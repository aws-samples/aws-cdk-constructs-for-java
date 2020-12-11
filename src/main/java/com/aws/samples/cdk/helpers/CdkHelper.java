package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.annotations.processors.CdkAutoWireProcessor;
import io.vavr.Tuple;
import io.vavr.control.Try;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class CdkHelper {
    public static final String NO_SEPARATOR = "";
    private static Optional<String> stackName = Optional.empty();
    private static Optional<Random> random = Optional.empty();
    private static Optional<Map<String, String>> arguments = Optional.ofNullable(System.getenv());

    public static void setStackName(String stackName) {
        CdkHelper.stackName = Optional.of(stackName);
    }

    public static Optional<Map<String, String>> getArguments() {
        return arguments;
    }

    public static String getRandomId() {
        return "id" + nextRandomLong();
    }

    private static Long nextRandomLong() {
        if (!random.isPresent()) {
            stackName.orElseThrow(() -> new RuntimeException("Stack name must be present"));

            random = Optional.of(new Random(UUID.nameUUIDFromBytes(CdkHelper.stackName.get().getBytes()).getLeastSignificantBits()));
        }

        return random.get().nextLong();
    }

    public static String getFileHash(File file) {
        return Try.withResources(() -> new FileInputStream(file))
                .of(CdkHelper::digest)
                .map(CdkHelper::digestToString)
                // Throw an exception if this fails, we can't continue if it does
                .get();
    }

    private static String digestToString(MessageDigest messageDigest) {
        return new BigInteger(messageDigest.digest())
                // Make sure the value isn't negative
                .abs()
                // Get a base-36 value to keep it compact
                .toString(36);
    }

    private static MessageDigest digest(FileInputStream fileInputStream) {
        MessageDigest messageDigest = Try.of(() -> MessageDigest.getInstance("SHA-256")).get();

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = Try.of(() -> fileInputStream.read(byteArray)).get()) != -1) {
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
                .map(stream -> stream.collect(Collectors.toList()))
                // If we threw an exception then just return an empty list
                .getOrElse(ArrayList::new);
    }
}