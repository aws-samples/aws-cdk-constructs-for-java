package com.aws.samples.cdk.annotations.processors;

import com.google.auto.service.AutoService;
import io.vavr.control.Try;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.aws.samples.cdk.annotations.CdkAutoWire")
public class CdkAutoWireProcessor extends AbstractProcessor {
    public static final String RESOURCE_FILE = "META-INF/services/" + CdkAutoWireProcessor.class.getName();

    private List<String> cdkAutoWireClassList = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            generateConfigFiles();
            cdkAutoWireClassList = new ArrayList<>();
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
                .getOrElse(new ArrayList<>());

        // Throw an exception if opening the output stream fails
        OutputStream outputStream = Try.of(() -> filer.createResource(StandardLocation.CLASS_OUTPUT, "", RESOURCE_FILE))
                .mapTry(FileObject::openOutputStream)
                .get();

        cdkAutoWireClassList.addAll(existingCdkAutoWiredClasses);
        cdkAutoWireClassList = cdkAutoWireClassList.stream().distinct().collect(Collectors.toList());

        String output = String.join("\n", cdkAutoWireClassList);
        Try.run(() -> outputStream.write(output.getBytes(StandardCharsets.UTF_8))).get();
        Try.run(outputStream::close);
    }

    private List<String> readFile(InputStream inputStream) {
        List<String> output = new ArrayList<>();
        new Scanner(inputStream).forEachRemaining(output::add);

        return output;
    }

    private boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream()
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .map(Element::getSimpleName)
                .map(Object::toString)
                .forEach(name -> cdkAutoWireClassList.add(name));

        return true;
    }
}