plugins {
    kotlin("jvm") version "1.4.21-2"
    id("application")
    id("java")
    id("idea")
    id("java-library")
    id("maven")
}

group = "local"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

val gradleDependencyVersion = "6.8.1"

tasks.wrapper {
    gradleVersion = gradleDependencyVersion
    distributionType = Wrapper.DistributionType.ALL
}

tasks.distZip { enabled = true }
tasks.distTar { enabled = true }

// Specify all of our dependency versions
val awsCdkVersion = "1.86.0"
val vavrVersion = "0.10.3"
val slf4jVersion = "2.0.0-alpha1"
val jcabiVersion = "0.19.0"
val commonsLangVersion = "3.11"
val commonsIoVersion = "2.8.0"
val ztZipVersion = "1.14"
val resultsIteratorForAwsJavaSdkVersion = "11.0.2"
val daggerVersion = "2.31.2"
val junitVersion = "4.13.1"
val autoServiceVersion = "1.0-rc7"
val javaPoetVersion = "1.13.0"
val immutablesValueVersion = "2.8.9-ea-1"
val awsLambdaJavaCoreVersion = "1.2.1"
val awsLambdaServletVersion = "0.2.1"

repositories {
    mavenCentral()
    maven(url = "https://repo.gradle.org/gradle/libs-releases-local/")
    maven(url = "https://jitpack.io")
}

dependencies {
    // Auto service code generation
    annotationProcessor("com.google.auto.service:auto-service:$autoServiceVersion")
    api("com.google.auto.service:auto-service-annotations:$autoServiceVersion")

    // Immutables with JSON support
    annotationProcessor("org.immutables:value:$immutablesValueVersion")
    annotationProcessor("org.immutables:gson:$immutablesValueVersion")
    implementation("org.immutables:value:$immutablesValueVersion")
    implementation("org.immutables:gson:$immutablesValueVersion")

    // Dagger code generation
    annotationProcessor("com.google.dagger:dagger-compiler:$daggerVersion")

    // Dependency injection with Dagger
    implementation("com.google.dagger:dagger:$daggerVersion")

    implementation("org.slf4j:slf4j-log4j12:$slf4jVersion")
    implementation("com.jcabi:jcabi-log:$jcabiVersion")

    implementation("software.amazon.awscdk:core:$awsCdkVersion")
    api("software.amazon.awscdk:iam:$awsCdkVersion")
    api("software.amazon.awscdk:sqs:$awsCdkVersion")
    api("software.amazon.awscdk:iot:$awsCdkVersion")
    api("software.amazon.awscdk:lambda:$awsCdkVersion")
    api("software.amazon.awscdk:dynamodb:$awsCdkVersion")
    api("software.amazon.awscdk:apigateway:$awsCdkVersion")
    implementation("io.vavr:vavr:$vavrVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")
    implementation("commons-io:commons-io:$commonsIoVersion")
    api("com.github.awslabs:results-iterator-for-aws-java-sdk:$resultsIteratorForAwsJavaSdkVersion")
    implementation("com.amazonaws:aws-lambda-java-core:$awsLambdaJavaCoreVersion")
    api("com.github.aws-samples:aws-lambda-servlet:$awsLambdaServletVersion")

    testImplementation("junit:junit:$junitVersion")
}
