package com.acepero13.research.profilesimilarity.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The ListUtils class provides utility methods for working with lists.
 */
public class ListUtils {

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private ListUtils() {
    }

    /**
     * Returns a stream of results obtained by applying the specified mapper function
     * to corresponding elements from two input lists. The input lists must have the same size.
     *
     * @param <T>    the type of elements in the input lists
     * @param <R>    the type of elements in the resulting stream
     * @param first  the first input list
     * @param second the second input list
     * @param mapper the function that maps elements from the input lists to the output stream
     * @return a stream of results obtained by applying the specified mapper function
     * to corresponding elements from two input lists
     * @throws IllegalStateException if the size of the input lists is different
     */
    public static <T, R> Stream<R> zip(List<T> first, List<T> second, BiFunction<T, T, R> mapper) throws IllegalStateException {
        if (first.size() != second.size()) {
            throw new IllegalStateException("Size of the lists are different. First size: " + first.size() + " second size: " + second.size());
        }
        return IntStream.range(0, first.size())
                .mapToObj(i -> mapper.apply(first.get(i), second.get(i)));
    }

    /**
     * Returns a new list obtained by padding the input list with the specified padding element
     * to reach the specified size. If the input list is already larger than the specified size,
     * it is returned unmodified.
     *
     * @param <T>      the type of elements in the input and output lists
     * @param features the input list to pad
     * @param padding  the element to use for padding
     * @param n        the desired size of the output list
     * @return a new list obtained by padding the input list with the specified padding element
     * to reach the specified size
     */
    public static <T> List<T> padding(List<T> features, T padding, int n) {
        List<T> newFeatures = new ArrayList<>(features);
        for (int i = 0; i < n; i++) {
            newFeatures.add(padding);
        }
        return Collections.unmodifiableList(newFeatures);
    }

    /**
     * Returns a stream of tuples obtained by pairing corresponding elements from two input lists.
     * The input lists must have the same size.
     *
     * @param <T>    the type of elements in the first input list
     * @param <R>    the type of elements in the second input list
     * @param first  the first input list
     * @param second the second input list
     * @return a stream of tuples obtained by pairing corresponding elements from two input lists
     * @throws IllegalStateException if the size of the input lists is different
     */
    public static <T, R> Stream<Tuple<T, R>> zip(List<T> first, List<R> second) {
        if (first.size() != second.size()) {
            throw new IllegalStateException("Size of the lists are different. First size: " + first.size() + " second size: " + second.size());
        }
        return IntStream.range(0, first.size())
                .mapToObj(i -> Tuple.of(first.get(i), second.get(i)));
    }
}