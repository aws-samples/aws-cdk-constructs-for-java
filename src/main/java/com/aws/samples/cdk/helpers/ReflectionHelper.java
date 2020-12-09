package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.HasIamPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import io.vavr.control.Try;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.PolicyStatement;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionHelper {
    public static final String HANDLE_REQUEST = "handleRequest";

    public static <T> Stream<Class<T>> findClassesInJarImplementingInterface(File file, Class<T> interfaceClass) {
        JarFile jarFile = Try.of(() -> new JarFile(file)).get();

        List<JarEntry> jarEntries = Collections.list(jarFile.entries());

        return jarEntries.stream()
                // Only look at classes
                .filter(entry -> entry.getName().endsWith(".class"))
                // Remove the file extension
                .map(entry -> entry.getName().replace(".class", ""))
                // Make sure the class isn't the interface itself
                .filter(className -> !className.endsWith(interfaceClass.getSimpleName()))
                // Replace the slash characters in the path with dots so it has a real class name
                .map(name -> name.replace('/', '.'))
                // Attempt to load the class
                .map(clazz -> ReflectionHelper.safeLoadClass(file, clazz))
                // Removed any classes that failed to load
                .filter(Try::isSuccess)
                // Get the raw class
                .map(Try::get)
                // Be sure we're only using classes that can be assigned to our interface class
                .filter(interfaceClass::isAssignableFrom)
                // Cast the raw class to a class that extends the requested interface
                .map(clazz -> (Class<T>) clazz);
    }

    private static Try<? extends Class<?>> safeLoadClass(File file, String name) {
        try {
            return Try.of(() -> getJarFileClassLoader(file).loadClass(name));
        } catch (LinkageError e) {
            return Try.failure(new RuntimeException(e));
        }
    }

    private static URLClassLoader getJarFileClassLoader(File file) {
        URL url = Try.of(() -> new URL("jar:file:" + file.getPath() + "!/")).get();

        return URLClassLoader.newInstance(new URL[]{url});
    }

    private static Try<HasIamPermissions> instantiateHasIamPermissions(File file, String name) {
        return safeLoadClass(file, name)
                .mapTry(clazz -> clazz.getDeclaredConstructor().newInstance())
                .mapTry(clazz -> (HasIamPermissions) clazz);
    }

    private static List<PolicyStatement> getPolicyStatementsForClass(File file, String name) {
        return instantiateHasIamPermissions(file, name)
                .map(HasIamPermissions::getPermissions)
                .getOrElseGet(throwable -> new ArrayList<>())
                .stream()
                .map(IamPermission::getPolicyStatement)
                .collect(Collectors.toList());
    }

    public static Optional<PolicyDocumentProps> getOptionalPolicyDocumentForClass(File file, String name) {
        return getOptionalPolicyDocumentForPolicyStatements(getPolicyStatementsForClass(file, name));
    }

    private static Optional<PolicyDocumentProps> getOptionalPolicyDocumentForPolicyStatements(List<PolicyStatement> policyStatements) {
        if (policyStatements.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(PolicyDocumentProps.builder()
                .statements(policyStatements)
                .build());
    }
}