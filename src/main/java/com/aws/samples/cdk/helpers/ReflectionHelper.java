package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.HasIamPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.PolicyStatement;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class ReflectionHelper {
    public static final String HANDLE_REQUEST = "handleRequest";

    public static <T> List<Class<T>> findClassesInJarImplementingInterface(File file, Class<T> interfaceClass) {
        return CdkHelper.getCdkAutoWiredClassList(file)
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
                .getOrElseGet(throwable -> List.empty())
                .map(IamPermission::getPolicyStatement);
    }

    public static Option<PolicyDocumentProps> getPolicyDocumentForClassOption(File file, String name) {
        return getPolicyDocumentForPolicyStatementsOption(getPolicyStatementsForClass(file, name));
    }

    private static Option<PolicyDocumentProps> getPolicyDocumentForPolicyStatementsOption(List<PolicyStatement> policyStatements) {
        if (policyStatements.size() == 0) {
            return Option.none();
        }

        return Option.of(PolicyDocumentProps.builder()
                .statements(policyStatements.asJava())
                .build());
    }
}
