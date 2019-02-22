package com.zzqangel.ddd.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class AnnotationUtils {

    public static List<Annotation> getFiledAnnotations(Field field) {
        return Arrays.asList(field.getDeclaredAnnotations());
    }

    public static <T extends Annotation> T getFiledAnnotation(Field field, Class<T> annotationClass) {
        return field.getDeclaredAnnotation(annotationClass);
    }
}
