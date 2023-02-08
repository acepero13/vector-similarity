package com.acepero13.research.profilesimilarity.utils;

import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public class VectorCollector implements Collector<Double, List<Double>, Vector<Double>> {
    public static VectorCollector toVector(){
        return new VectorCollector();
    }
    @Override
    public Supplier<List<Double>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Double>, Double> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<Double>> combiner() {
        return (list1, list2) ->
        {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<Double>, Vector<Double>> finisher() {
        return DoubleVector::of;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }


}
