package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.annotations.processors.CdkAutoWireProcessor;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.CharSeq;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import software.amazon.awscdk.core.App;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class CdkHelper {
    public static final String NO_SEPARATOR = "";
    private static Option<String> stackNameOption = Option.none();
    private static Lazy<Random> lazyRandom = Lazy.of(() -> new Random(UUID.nameUUIDFromBytes(CdkHelper.getStackName().getBytes()).getLeastSignificantBits()));
    private static Map<String, String> arguments = HashMap.ofAll(Option.of(System.getenv()).getOrElse(java.util.HashMap::new));

    public static void setStackName(String stackName) {
        if (!stackName.isEmpty()) {
            throw new RuntimeException("Stack name already set [" + stackName + "]. It can not be changed.");
        }

        CdkHelper.stackNameOption = Option.of(stackName);
    }

    public static String getStackName() {
        if (stackNameOption.isEmpty()) {
            setStackName(camelCaseToKebabCase(getMainClassName()));
        }

        return stackNameOption.get();
    }

    private static final Lazy<App> lazyApp = Lazy.of(App::new);

    public static App getApp() {
        return lazyApp.get();
    }

    public static Map<String, String> getArguments() {
        return arguments;
    }

    public static String getRandomId() {
        return "id" + nextRandomLong();
    }

    private static Long nextRandomLong() {
        stackNameOption.getOrElseThrow(() -> new RuntimeException("Stack name must be present"));

        return lazyRandom.get().nextLong();
    }

    public static String getJarFileHash(File file) {
        JarFile jarFile = Try.of(() -> new JarFile(file)).get();

        java.util.List<InputStream> inputStreamList = jarFile.stream()
                // Sort the entries by name so they are ordered consistently
                .sorted(Comparator.comparing(ZipEntry::getName))
                // Get an input stream for each entry
                .map(jarEntry -> toInputStream(jarFile, jarEntry))
                // Collect them into a list so they can be added to the sequence input stream
                .collect(Collectors.toList());

        // Create a sequence input stream of all of the ordered files
        SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(inputStreamList));

        // Hash just the content (avoids ZIP file permission and timestamp data from changing the hash)
        return digestToString(digest(sequenceInputStream));
    }

    private static InputStream toInputStream(JarFile jarFile, JarEntry jarEntry) {
        return Try.of(() -> jarFile.getInputStream(jarEntry)).get();
    }

    private static String digestToString(MessageDigest messageDigest) {
        return new BigInteger(messageDigest.digest())
                // Make sure the value isn't negative
                .abs()
                // Get a base-36 value to keep it compact
                .toString(36);
    }

    private static MessageDigest digest(InputStream inputStream) {
        MessageDigest messageDigest = Try.of(() -> MessageDigest.getInstance("SHA-256")).get();

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = Try.of(() -> inputStream.read(byteArray)).get()) != -1) {
            messageDigest.update(byteArray, 0, bytesCount);
        }

        return messageDigest;
    }

    public static List<String> getCdkAutoWiredClassList(File file) {
        return Try.of(() -> new JarFile(file))
                // Get the JAR file object and the JAR entry together
                .map(jarFile -> Tuple.of(jarFile, jarFile.getJarEntry(CdkAutoWireProcessor.RESOURCE_FILE)))
                // Get the input stream for the JAR entry from the JAR file
                .mapTry(tuple -> tuple._1.getInputStream(tuple._2))
                .filter(Objects::nonNull)
                // Get a reader and extract the lines
                .map(inputStream -> new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .map(BufferedReader::new)
                .map(BufferedReader::lines)
                // Convert the lines to a list
                .map(List::ofAll)
                // If we threw an exception then just return an empty list
                .getOrElse(List.empty());
    }

    public static String camelCaseToKebabCase(String camelCase) {
        if ((camelCase == null) || (camelCase.length() == 0)) {
            // Nothing to do
            return camelCase;
        }

        // First character must be forced to lower case to work with class names
        String output = camelCase.substring(0, 1).toLowerCase();

        if (camelCase.length() == 1) {
            // Nothing more to do if it is only one character long
            return output;
        }

        // Add the rest of the input string back
        output = output + camelCase.substring(1);

        return CharSeq.of(output)
                // Convert each character to a string in kebab case (either just the original character or an
                //   underscore followed by the lowercase version of the original character)
                .map(CdkHelper::charToKebab)
                // Combine all of the strings into a single string to return
                .fold("", (a1, a2) -> a1 + a2);
    }

    private static String charToKebab(char value) {
        if (!Character.isUpperCase(value)) {
            return String.valueOf(value);
        }

        return "_" + Character.toLowerCase(value);
    }

    // Guidance from: https://stackoverflow.com/a/36949543/796579
    private static String getMainClassName() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        if (trace.length <= 0) {
            return "Unknown";
        }

        String className = trace[trace.length - 1].getClassName();

        if (!className.contains(".")) {
            return className;
        }

        return className.substring(className.lastIndexOf(".") + 1);
    }
}
