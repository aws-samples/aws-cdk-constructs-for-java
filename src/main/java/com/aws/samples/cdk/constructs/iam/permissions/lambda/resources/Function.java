package com.aws.samples.cdk.constructs.iam.permissions.lambda.resources;

import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotDataPlaneResource;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class Function implements IotDataPlaneResource {
    public abstract String getFunctionName();

    @Override
    public String getResourceType() {
        return "function";
    }

    @Override
    public String getResourceValue() {
        return getFunctionName();
    }
}
