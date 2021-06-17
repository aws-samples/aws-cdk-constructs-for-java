package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.iam.permissions.HasIamPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.lambda.servlet.automation.GeneratedClassFinder;
import com.aws.samples.lambda.servlet.automation.GeneratedClassInfo;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.services.iam.PolicyDocumentProps;
import software.amazon.awscdk.services.iam.PolicyStatement;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class ReflectionHelper {
    private static final Logger log = LoggerFactory.getLogger(ReflectionHelper.class);
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
        } catch (UnsupportedClassVersionError exception) {
            logUnsupportedClassVersionError(file, name);

            // This is fatal, rethrow it
            throw exception;
        }
    }

    private static void logUnsupportedClassVersionError(File file, String name) {
        String path = Try.of(file::getCanonicalFile)
                .map(File::getAbsolutePath)
                .getOrElse(file::getAbsolutePath);

        log.error("The class [" + name + "] could not be loaded from [" + path + "]");
        log.error("The current JVM is older than the class file version compiled in this JAR");
        log.error("Try updating the JVM version (full exception below)");
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
                .onFailure(Exception.class, exception -> ReflectionHelper.logAndThrow(exception, file))
                .map(HasIamPermissions::getPermissions)
                .getOrElseGet(throwable -> List.empty())
                .map(IamPermission::getPolicyStatement);
    }

    private static void logAndThrow(Exception exception, File file) {
        exception.printStackTrace();

        // Sleep for 1 second to let the stack trace flush
        Try.run(() -> Thread.sleep(1000));

        log.error("-- ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION --");
        log.error("");
        log.error("Failed to instantiate the class from [" + file.getAbsolutePath() + "], this may be caused by static fields throwing exceptions (e.g. looking for Lambda environment variables outside of the Lambda runtime environment)");
        log.error("If the class needs to do this at runtime try wrapping the calls in Lazy.of(() -> ...) so they're only evaluated at runtime.");
        log.error("");
        log.error("-- ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION ATTENTION --");

        System.exit(1);
    }

    public static Option<PolicyDocumentProps> getPolicyDocumentForClassOption(File file, String name) {
        return getPolicyDocumentForPolicyStatementsOption(getPolicyStatementsForClass(file, name));
    }

    private static Option<PolicyDocumentProps> getPolicyDocumentForPolicyStatementsOption(List<PolicyStatement> policyStatements) {
        if (policyStatements.isEmpty()) {
            return Option.none();
        }

        return Option.of(PolicyDocumentProps.builder()
                .statements(policyStatements.asJava())
                .build());
    }

    public static List<GeneratedClassInfo> getGeneratedClassInfo(File file) {
        JarFile jarFile = Try.of(() -> new JarFile(file)).get();

        return new GeneratedClassFinder().getGeneratedClassList(jarFile);
    }

    public static String getLastClassName(String className) {
        String[] splitClassName = className.split("\\.");
        return splitClassName[splitClassName.length - 1];
    }
}
