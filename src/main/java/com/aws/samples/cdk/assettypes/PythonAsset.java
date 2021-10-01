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
                        // Conditionally try to install requirements.txt, not all functions will have one
                        "(" + String.join("&& ",
                                "ls requirements.txt",
                                "mkdir temp-deployment",
                                "pip3 install -r requirements.txt -t temp-deployment",
                                "cd temp-deployment",
                                "zip -r /asset-output/output.zip .",
                                "cd ..",
                                "rm -rf temp-deployment")
                                // Call OR true to prevent a missing requirements.txt file from stopping CDK
                                + ") || true"
                )
        );

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
