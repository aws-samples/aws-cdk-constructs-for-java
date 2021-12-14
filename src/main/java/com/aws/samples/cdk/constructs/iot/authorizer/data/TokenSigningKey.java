package com.aws.samples.cdk.constructs.iot.authorizer.data;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Function;

@Gson.TypeAdapters
@Value.Immutable
public abstract class TokenSigningKey {
    public static Builder builder() {
        return new Builder();
    }

    public abstract String getName();

    public abstract Either<File, String> getRawKey();

    public String getKey() {
        return getRawKey()
                .mapLeft(File::toPath)
                .mapLeft(path -> Try.of(() -> Files.readAllBytes(path)))
                .mapLeft(result -> result.getOrElseThrow(() -> new RuntimeException("Couldn't read key file")))
                // Convert the bytes to a string OR just return the existing string
                .fold(String::new, Function.identity());
    }

    public static class Builder extends ImmutableTokenSigningKey.Builder {
    }
}
