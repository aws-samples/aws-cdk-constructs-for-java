package com.aws.samples.cdk.constructs.iam.permissions.iot.controlplane.actions;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;
import com.aws.samples.cdk.constructs.iam.permissions.IamPermission;
import com.aws.samples.cdk.constructs.iam.permissions.IamResource;
import com.aws.samples.cdk.constructs.iam.permissions.SharedPermissions;
import com.aws.samples.cdk.constructs.iam.permissions.iot.dataplane.resources.IotAllResources;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Gson.TypeAdapters
@Value.Immutable
public abstract class CreateCertificateFromCsr implements IamPermission {
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Tuple2<IamAction, IamResource> getActionAndResource() {
        IamAction iamAction = new IamAction() {
            @Override
            public String getIamString() {
                return SharedPermissions.IOT_CREATE_CERTIFICATE_FROM_CSR_PERMISSION;
            }

            @Override
            public IamResource getIamResource() {
                return new IotAllResources();
            }
        };

        return Tuple.of(iamAction, iamAction.getIamResource());
    }

    public static class Builder extends ImmutableCreateCertificateFromCsr.Builder {
    }
}
