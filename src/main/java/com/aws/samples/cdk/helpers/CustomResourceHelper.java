package com.aws.samples.cdk.helpers;

import com.aws.samples.cdk.constructs.autowired.cloudformation.CustomResourceFunction;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awscdk.core.CustomResource;
import software.amazon.awscdk.core.CustomResourceProps;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;

import java.io.File;

import static com.aws.samples.cdk.helpers.LambdaHelper.getFunction;
import static com.aws.samples.cdk.helpers.ReflectionHelper.findClassesInJarImplementingInterface;
import static com.aws.samples.cdk.helpers.ReflectionHelper.getLastClassName;

/**
 * Functions that specifically help with creating Lambda backed custom resources
 */
public class CustomResourceHelper {
    public static final String ACTION = "action";
    public static final String CREATE = "create";
    public static final String DELETE = "delete";
    public static final String UPDATE = "update";
    private static final Logger log = LoggerFactory.getLogger(CustomResourceHelper.class);
    private static final HashMap<String, String> lambdaFunctionCreatePayload = HashMap.of(ACTION, CREATE);
    private static final HashMap<String, String> lambdaFunctionDeletePayload = HashMap.of(ACTION, DELETE);
    private static final HashMap<String, String> lambdaFunctionUpdatePayload = HashMap.of(ACTION, UPDATE);

    private static List<Class<CustomResourceFunction>> findCustomResourcesInJar(File file) {
        return findClassesInJarImplementingInterface(file, CustomResourceFunction.class);
    }

    public static List<CustomResource> getCustomResources(Stack stack, File file, Option<CustomResourceProps.Builder> customResourcePropsBuilderOption, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        return findCustomResourcesInJar(file)
                .map(clazz -> getCustomResource(stack, file, clazz.getName(), customResourcePropsBuilderOption, functionPropsBuilderOption));
    }

    public static CustomResource getCustomResource(Stack stack, File file, String className, Option<CustomResourceProps.Builder> customResourcePropsBuilderOption, Option<FunctionProps.Builder> functionPropsBuilderOption) {
        // Create the function
        Function lambda = getFunction(stack, file, className, functionPropsBuilderOption);

        // Build the custom resource property and use the builder passed in if there was one
        CustomResourceProps customResourceProps = customResourcePropsBuilderOption
                .getOrElse(CustomResourceProps.builder())
                .serviceToken(lambda.getFunctionArn())
                .build();

        return new CustomResource(stack, getLastClassName(className), customResourceProps);
    }
}
