package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.exceptions.ArgumentException;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class FeaturesHelper {
    private FeaturesHelper() {


    }

    public static List<Feature<?>> features(Object[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected one argument" + Arrays.toString(args));
        }
        List<Feature<?>> feats = (List<Feature<?>>) Optional.ofNullable(args[0])
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .filter(l -> l.stream().allMatch(f -> f instanceof Feature))
                .orElseThrow()
                .stream()
                .map(Feature.class::cast)
                .collect(Collectors.toList());
        return feats;
    }

    public static boolean checkArguments(Object[] arguments,
                                         int expectedNumberOfArguments,
                                         Predicate<Tuple<Integer, ? extends Class<?>>> predicate
    ) throws ArgumentException {
        if(arguments.length != expectedNumberOfArguments) {
            throw new ArgumentException("Expected number of arguments: <" + expectedNumberOfArguments + ">. But got: <" + arguments.length + ">");
        }


        return IntStream.range(0, arguments.length)
                .mapToObj(i -> Tuple.of(i, arguments[i].getClass()))
                .anyMatch(predicate);
    }

    public static boolean checkArguments(Object[] arguments, Predicate<Class<?>> predicate) throws ArgumentException {
        if (arguments.length != 1) {
            throw new ArgumentException("Expected number of arguments: <1>. But got: <" + arguments.length + ">");
        }
        return predicate.test(arguments[0].getClass());
    }

    public static Predicate<Tuple<Integer, ? extends Class<?>>> oneOf(Class<?> ...expected) {
        return tuple -> Stream.of(expected).anyMatch(c -> c.equals(tuple.second()));
    }

    public static Predicate<Class<?>> exactly(Class<?> expected) {
        return expected::equals;
    }


}
