package com.aws.samples.cdk.assettypes;

import io.vavr.collection.List;
import software.amazon.awscdk.core.BundlingOptions;
import software.amazon.awscdk.core.BundlingOutput;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.assets.AssetOptions;

public class PythonAsset {
    public static AssetOptions getAssetOptions(Runtime runtime) {
        List<String> packagingCommandList = List.of(
                "/bin/sh",
                "-c",
                String.join("&&",
                        "rm -rf temp-deployment",
                        "rm -f /asset-output/output.zip",
                        "zip -r /asset-output/output.zip .",
                        "mkdir temp-deployment",
                        // Conditionally try to install requirements.txt, not all functions will have one
                        "(ls requirements.txt && pip3 install -r requirements.txt -t temp-deployment)",
                        "cd temp-deployment",
                        "zip -r /asset-output/output.zip .",
                        "cd ..",
                        "rm -rf temp-deployment"));

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
