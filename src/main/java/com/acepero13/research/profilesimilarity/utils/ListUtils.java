package com.acepero13.research.profilesimilarity.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ListUtils {
    private ListUtils() {
    }

    public static <T, R> Stream<R> zip(List<T> first, List<T> second, BiFunction<T, T, R> mapper) throws IllegalStateException {
        if (first.size() != second.size()) {
            throw new IllegalStateException("Size of the lists are different. First size: " + first.size() + " second size: " + second.size());
        }
        return IntStream.range(0, first.size())
                .mapToObj(i -> mapper.apply(first.get(i), second.get(i)));
    }

    public static <T> List<T> padding(List<T> features, T padding, int n) {
        List<T> newFeatures = new ArrayList<>(features);
        for (int i = 0; i < n; i++) {
            newFeatures.add(padding);
        }
        return Collections.unmodifiableList(newFeatures);
    }

    public static <T, R> Stream<Tuple<T, R>> zip(List<T> first, List<R> second) {
        if (first.size() != second.size()) {
            throw new IllegalStateException("Size of the lists are different. First size: " + first.size() + " second size: " + second.size());
        }
        return IntStream.range(0, first.size())
                .mapToObj(i -> Tuple.of(first.get(i), second.get(i)));
    }
}
