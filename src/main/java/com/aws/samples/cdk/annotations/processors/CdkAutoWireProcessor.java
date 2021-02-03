package com.aws.samples.cdk.annotations.processors;

import com.google.auto.service.AutoService;
import io.vavr.collection.List;
import io.vavr.control.Try;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.aws.samples.cdk.annotations.CdkAutoWire")
public class CdkAutoWireProcessor extends AbstractProcessor {
    public static final String RESOURCE_FILE = "META-INF/services/" + CdkAutoWireProcessor.class.getName();

    private List<String> cdkAutoWireClassList = List.empty();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
            cdkAutoWireClassList = List.empty();
        } else {
            processAnnotations(annotations, roundEnv);
        }

        return true;
    }

    // Some guidance from https://github.com/google/auto/blob/6bed859f25a8f164506b9fa7437bdbd32ccf1cd0/service/processor/src/main/java/com/google/auto/service/processor/AutoServiceProcessor.java#L162
    private void generateConfigFiles() {
        Filer filer = processingEnv.getFiler();

        List<String> existingCdkAutoWiredClasses = Try.of(() -> filer.getResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE))
                .mapTry(FileObject::openInputStream)
                .map(this::readFile)
                .getOrElse(List.empty());

        // Throw an exception if opening the output stream fails
        OutputStream outputStream = Try.of(() -> filer.createResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE))
                .mapTry(FileObject::openOutputStream)
                .get();

        cdkAutoWireClassList = cdkAutoWireClassList.appendAll(existingCdkAutoWiredClasses);
        cdkAutoWireClassList = cdkAutoWireClassList.distinct();

        String output = String.join("\n", cdkAutoWireClassList);
        Try.run(() -> outputStream.write(output.getBytes(StandardCharsets.UTF_8))).get();
        Try.run(outputStream::close);
    }

    private List<String> readFile(InputStream inputStream) {
        ArrayList<String> output = new ArrayList<>();
        new Scanner(inputStream).forEachRemaining(output::add);

        return List.ofAll(output);
    }

    private boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        cdkAutoWireClassList = cdkAutoWireClassList.appendAll(annotations.stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .map(element -> String.join(".", element.getEnclosingElement().toString(), element.getSimpleName()))
                .map(Object::toString)
                .collect(Collectors.toList()));

        return true;
    }
}
