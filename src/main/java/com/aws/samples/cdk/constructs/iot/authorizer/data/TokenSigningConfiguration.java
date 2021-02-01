package com.aws.samples.cdk.constructs.iot.authorizer.data;

import io.vavr.collection.List;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Map;
import java.util.stream.Collectors;

@Gson.TypeAdapters
@Value.Immutable
public abstract class TokenSigningConfiguration {
    public abstract List<TokenSigningKey> getTokenSigningKeys();

    public abstract String getTokenKeyName();

    public Map<String, String> getMap() {
        return getTokenSigningKeys()
                .collect(Collectors.toMap(TokenSigningKey::getName, TokenSigningKey::getKey));
    }
}
