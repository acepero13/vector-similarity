package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@EqualsAndHashCode
@ToString
public class DoubleVector implements Vector<Double> {
    private final List<Double> features;
    private final int size;

    public DoubleVector(List<Double> arr) {
        this.features = Objects.requireNonNull(arr, "Features cannot be null");
        this.size = features.size();
    }

    public DoubleVector(Double[] arr) {
        this(List.of(arr));
    }

    public static DoubleVector of(Double... features) {
        return new DoubleVector(features);
    }

    public static DoubleVector of(List<Double> features) {
        return new DoubleVector(features);
    }

    public static DoubleVector of(Feature<?>... features) {
        return ofFeatures(List.of(features));
    }

    public static DoubleVector of(Integer... features) {
        return new DoubleVector(Stream.of(features)
                .map(Integer::doubleValue)
                .collect(Collectors.toList()));
    }

    public static DoubleVector ofFeatures(List<Feature<?>> features) {
        return new DoubleVector(features.stream()
                .map(Feature::featureValue)
                .collect(Collectors.toList()));
    }

    @Override
    public Double norm() {
        return Math.sqrt(
                features.stream()
                        .mapToDouble(f -> Math.pow(f, 2))
                        .sum()
        );
    }

    @Override
    public Double cosine(Vector<Double> another) throws VectorException {
        Double denominator = norm() * another.norm();
        if (denominator == 0) {
            return 0.0;
        }
        return dot(another) / denominator;
    }

    @Override
    public Double dot(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);
        return zip(another)
                .map(t -> t.first() * t.second())
                .reduce(0.0, Double::sum);
    }

    @Override
    public Double distanceTo(Vector<Double> another) throws VectorException {
        return subtract(another).norm();
    }

    @Override
    public DoubleVector add(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);

        return new DoubleVector(zip(another)
                .map(t -> t.first() + t.second())
                .collect(Collectors.toList())
        );
    }

    @Override
    public DoubleVector subtract(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);
        return new DoubleVector(zip(another)
                .map(t -> t.first() - t.second())
                .collect(Collectors.toList()));
    }

    @Override
    public void checkSizeMatchWith(Vector<Double> another) {
        if (size != another.size()) {
            throw new VectorException("Vector length do not match. Vector length is: " + size + " and the other vector's length is: " + another.size());
        }
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<Double> another) {
        return IntStream.range(0, size)
                .mapToObj(i -> Tuple.of(features.get(i), another.getFeature(i)));
    }

    @Override
    public DoubleVector multiply(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);
        return new DoubleVector(zip(another)
                .map(t -> t.first() * t.second())
                .collect(Collectors.toList()));
    }

    @Override
    public Double getFeature(int index) throws IndexOutOfBoundsException {
        return features.get(index);
    }


    @Override
    public int size() {
        return features.size();
    }

    @Override
    public MinMax minMax() {
        double min = features.stream().min(Double::compare).orElse(Double.MIN_VALUE - 1);
        double max = features.stream().max(Double::compare).orElse(Double.MIN_VALUE - 1);
        return new MinMax(min, max);
    }


    @Override
    public DoubleVector mapEach(List<UnaryOperator<Double>> mapper) {
        return new DoubleVector(IntStream.range(0, size)
                .mapToObj(i -> Tuple.of(features.get(i), mapper.get(i)))
                .map(t -> t.second().apply(t.first()))
                .collect(Collectors.toList()));
    }


}