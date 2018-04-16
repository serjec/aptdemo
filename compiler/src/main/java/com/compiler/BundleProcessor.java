package com.compiler;

import com.annotations.BindValue;
import com.annotations.BundleBind;
import com.annotations.InProgress;
import com.google.common.collect.Lists;
import com.google.googlejavaformat.java.Formatter;
import com.sun.tools.javac.code.Symbol;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"com.annotations.BundleBind", "com.annotations.InProgress"})
public class BundleProcessor extends AbstractProcessor {

    private String srcClassName;
    private String className;


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        final List<Class<? extends Annotation>> annotationsSupported = Lists.newArrayList(BundleBind.class, InProgress.class);

        for (Class<? extends Annotation> annotationHandler : annotationsSupported) {
            final Set<? extends Element> bundleBinds = roundEnv.getElementsAnnotatedWith(annotationHandler);

            if (bundleBinds.size() == 0) {
                return false;
            }

            for (Element classElement : bundleBinds) {
                TypeElement currentClass = (TypeElement) classElement;
                String targetPackage = ((Symbol.ClassSymbol) classElement).packge().toString();
                srcClassName = classElement.getSimpleName().toString();

                StringBuilder builder = new StringBuilder();

                addCode(builder, true,
                        "/** ",
                        " * Generated code by BundleProcessor, DO NOT MODIFY!!",
                        " * @author cristian.serje",
                        " */\n",

                        "package " + targetPackage + ";",
                        "import android.app.Activity;\n",
                        "import android.content.Intent;");

                className = srcClassName + "BundleBinder";

                builder.append("public class ")
                        .append(className)
                        .append("{\n\n"); // open class


                List<String> annotatedFields = getAnnotatedFields(currentClass);

                if(annotationHandler == InProgress.class){
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "NOPE!! You cant compile In Porgress Classes in Production!!");
                }

                builder.append(getClassMethods(annotatedFields));

                builder.append("}\n"); // close class


                try { // write the file
                    JavaFileObject source = processingEnv.getFiler().createSourceFile(targetPackage + "." + className);


                    Writer writer = source.openWriter();
                    String finalString;
                    try {
//                    finalString = builder.toString();
                        finalString = new Formatter().formatSource(builder.toString());
                    } catch (Exception e) {
                        //There's an error on the code, so it couldnt be formatted
                        finalString = builder.toString();
                    }
                    writer.write(finalString);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }
        return true;
    }

    private List<String> getAnnotatedFields(TypeElement typeElement) {

        List<String> bundleList = new ArrayList<>();

        final List<? extends Element> enclosedElements = typeElement.getEnclosedElements();

        //Loads Annotation information from pre-compiled file
        for (Element element : enclosedElements) {
            if (element.getAnnotation(BindValue.class) == null) {
                continue;
            }
            bundleList.add(element.toString());
        }

        return bundleList;
    }

    private String getClassMethods(List<String> annotatedFields) {
        StringBuilder mapResult = new StringBuilder();

        addCode(mapResult, true, "public static void openActivity(" + srcClassName + " srcActivity, Class target){",
                "Intent intent = new Intent(srcActivity, target);");

        for (String elementName : annotatedFields) {
            addCode(mapResult, true, "if(srcActivity." + elementName + ".getText().length()>0)intent.putExtra(\"" + elementName + "\"," + "srcActivity." + elementName + ".getText());");
        }
        addCode(mapResult, true, "srcActivity.startActivity(intent);", "}");

        return mapResult.toString();
    }

    private void addCode(StringBuilder builder, boolean autoBreak, String... statements) {
        for (String statement : statements) {
            builder.append(statement);
            if (autoBreak)
                builder.append("\n");
        }
    }
}
