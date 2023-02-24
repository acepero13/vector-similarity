package com.acepero13.research.profilesimilarity.utils;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * A custom {@link java.util.stream.Collector} that collects {@link java.lang.Double}s into a {@link Vector}.
 * <p>
 * The collector is unordered and its finisher function returns a {@link Vector} instance.
 */
public class VectorCollector implements Collector<Double, List<Double>, Vector<Double>> {

    /**
     * Creates a new instance of {@code VectorCollector}.
     *
     * @return A new instance of {@code VectorCollector}.
     */
    public static VectorCollector toVector() {
        return new VectorCollector();
    }

    /**
     * Supplies a new mutable container that will hold the collected elements.
     *
     * @return A {@link java.util.ArrayList} instance.
     */
    @Override
    public Supplier<List<Double>> supplier() {
        return ArrayList::new;
    }

    /**
     * Accumulates an element into the mutable container.
     *
     * @return A {@link java.util.List} object with a new element added to it.
     */
    @Override
    public BiConsumer<List<Double>, Double> accumulator() {
        return List::add;
    }

    /**
     * Combines the contents of two containers.
     *
     * @return A {@link java.util.List} object that is the result of combining two other lists.
     */
    @Override
    public BinaryOperator<List<Double>> combiner() {
        return (list1, list2) ->
        {
            list1.addAll(list2);
            return list1;
        };
    }

    /**
     * Transforms the container into the final result type.
     *
     * @return A {@link Vector} instance containing the elements collected into a list.
     */
    @Override
    public Function<List<Double>, Vector<Double>> finisher() {
        return DoubleVector::of;
    }

    /**
     * Returns the set of characteristics for this collector.
     *
     * @return An unmodifiable set containing only {@link java.util.stream.Collector.Characteristics#UNORDERED}.
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}