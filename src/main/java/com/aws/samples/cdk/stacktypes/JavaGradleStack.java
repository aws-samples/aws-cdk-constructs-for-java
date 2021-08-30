package com.aws.samples.cdk.stacktypes;

import com.aws.samples.cdk.helpers.CdkHelper;
import com.aws.samples.cdk.stacktypes.gradle.GradleSupport;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.services.lambda.AssetCode;
import software.amazon.awscdk.services.lambda.Code;

import java.io.File;
import java.util.jar.JarFile;

import static com.aws.samples.cdk.helpers.CdkHelper.NO_SEPARATOR;

public interface JavaGradleStack {
    String getProjectDirectory();

    default List<File> getProjectDirectoryFiles() {
        return List.of(new File(getProjectDirectory()));
    }

    default String getBuildOutputDirectory() {
        return "build/libs/";
    }

    String getOutputArtifactName();

    default Logger getLogger() {
        return LoggerFactory.getLogger(JavaGradleStack.class.getName());
    }

    default String build() {
        getLogger().info("Building artifacts...");
        getProjectDirectoryFiles().forEach(GradleSupport::buildJar);
        getLogger().info("Artifacts built");
        return CdkHelper.getJarFileHash(getOutputArtifactFile());
    }

    default String getOutputArtifactRelativePath() {
        return String.join(NO_SEPARATOR, getProjectDirectory(), getBuildOutputDirectory(), getOutputArtifactName());
    }

    default AssetCode getAssetCode() {
        return Code.fromAsset(getOutputArtifactRelativePath());
    }

    default File getOutputArtifactFile() {
        return new File(getOutputArtifactRelativePath());
    }

    default JarFile getArtifactFile() {
        return Try.of(() -> new JarFile(new File(getOutputArtifactRelativePath()))).get();
    }
}
