package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.annotations.processors.CdkAutoWireProcessor;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

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
    private static Option<String> stackName = Option.none();
    private static Option<Random> random = Option.none();
    private static Map<String, String> arguments = HashMap.ofAll(Option.of(System.getenv()).getOrElse(java.util.HashMap::new));

    public static void setStackName(String stackName) {
        CdkHelper.stackName = Option.of(stackName);
    }

    public static Map<String, String> getArguments() {
        return arguments;
    }

    public static String getRandomId() {
        return "id" + nextRandomLong();
    }

    private static Long nextRandomLong() {
        if (random.isEmpty()) {
            stackName.getOrElseThrow(() -> new RuntimeException("Stack name must be present"));

            random = Option.of(new Random(UUID.nameUUIDFromBytes(CdkHelper.stackName.get().getBytes()).getLeastSignificantBits()));
        }

        return random.get().nextLong();
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
}
