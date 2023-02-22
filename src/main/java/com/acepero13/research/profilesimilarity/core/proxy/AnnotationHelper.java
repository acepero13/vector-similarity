package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class AnnotationHelper {
    public static <A extends Annotation> Optional<A> getAnnotation(Object target, Class<A> annotationType) {
        return Arrays.stream(target.getClass().getAnnotations())
                .filter(a -> a.annotationType().equals(annotationType))
                .map(annotationType::cast)
                .findFirst();
    }

    public static <A extends Annotation> List<Tuple<Field, A>> getAnnotatedFields(Object target, Class<A> annotationType) {
        return Stream.of(target.getClass().getDeclaredFields())
                .map(a -> fields(a, annotationType))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    private static <A extends Annotation> Optional<Tuple<Field, A>> fields(Field field, Class<A> annotationType) {
        return Stream.of(field.getAnnotations())
                .filter(a -> a.annotationType().equals(annotationType))
                .map(a -> Tuple.of(field, annotationType.cast(a)))
                .findFirst();

    }
}
