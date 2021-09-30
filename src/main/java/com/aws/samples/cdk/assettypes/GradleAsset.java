package com.aws.samples.cdk.assettypes;

import io.vavr.collection.List;
import software.amazon.awscdk.core.BundlingOptions;
import software.amazon.awscdk.core.BundlingOutput;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;

public class GradleAsset {
    public static AssetOptions getAssetOptions(Runtime runtime, String outputJar) {
        List<String> packagingCommandList = List.of(
                "/bin/sh",
                "-c",
                String.join("&&",
                        "./gradlew build",
                        "cp build/libs/" + outputJar + " /asset-output/"));

        BundlingOptions bundlingOptions = BundlingOptions.builder()
                .command(packagingCommandList.asJava())
                .image(runtime.getBundlingImage())
                .user("root")
                .outputType(BundlingOutput.ARCHIVED).build();

        return AssetOptions.builder()
                .bundling(bundlingOptions)
                .build();
    }
}
