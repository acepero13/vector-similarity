package com.acepero13.research.profilesimilarity.core.vectors;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A {@link Vector} implementation that represents a collection of double values.
 */
@EqualsAndHashCode
@ToString
@Log
public class DoubleVector implements Vector<Double> {
    private final List<Double> features;
    private final int size;

    private DoubleVector(List<Double> arr) {
        this.features = Objects.requireNonNull(arr, "Features cannot be null");
        this.size = features.size();
    }

    private DoubleVector(Double[] arr) {
        this(List.of(arr));
    }


    /**
     * Creates a new DoubleVector object from an array of double values.
     *
     * @param features the array of double values to create the DoubleVector object from.
     * @return a new DoubleVector object initialized with the specified values.
     */
    public static DoubleVector of(Double... features) {
        return new DoubleVector(features);
    }

    /**
     * Creates a new DoubleVector object from a list of double values.
     *
     * @param features the list of double values to create the DoubleVector object from.
     * @return a new DoubleVector object initialized with the specified values.
     */
    public static DoubleVector of(List<Double> features) {
        return new DoubleVector(features);
    }

    /**
     * Creates a new DoubleVector object from an array of integer values.
     *
     * @param features the array of integer values to create the DoubleVector object from.
     * @return a new DoubleVector object initialized with the specified values, where each integer value is converted to double.
     */
    public static DoubleVector of(Integer... features) {
        return new DoubleVector(Stream.of(features)
                .map(Integer::doubleValue)
                .collect(Collectors.toList()));
    }

    /**
     * Creates a new DoubleVector object from a list of features, where each feature provides its own value to the DoubleVector object.
     *
     * @param features the list of features to create the DoubleVector object from.
     * @return a new DoubleVector object initialized with the specified values, where each feature's value is collected into a list of doubles.
     */
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
    public Vector<Double> add(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);

        return zip(another)
                .map(t -> t.first() + t.second())
                .collect(VectorCollector.toVector());
    }

    @Override
    public Vector<Double> subtract(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);
        return zip(another)
                .map(t -> t.first() - t.second())
                .collect(VectorCollector.toVector());
    }

    @Override
    public void checkSizeMatchWith(Vector<Double> another) throws VectorException {
        if (size != another.size()) {
            log.warning("Vectors do not match");
            throw new VectorException("Vector length do not match. Vector length is: " + size + " and the other vector's length is: " + another.size());
        }
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<Double> another) {
        return IntStream.range(0, size)
                .mapToObj(i -> Tuple.of(features.get(i), another.getFeature(i)));
    }

    @Override
    public Vector<Double> multiply(Vector<Double> another) throws VectorException {
        checkSizeMatchWith(another);
        return zip(another)
                .map(t -> t.first() * t.second())
                .collect(VectorCollector.toVector());
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
        double min = features.stream()
                .min(Double::compare).orElse(Double.MIN_VALUE - 1);
        double max = features.stream()
                .max(Double::compare).orElse(Double.MIN_VALUE - 1);
        return new MinMax(min, max);
    }


    @Override
    public Vector<Double> divide(Vector<Double> another) {
        return zip(another)
                .map(t -> t.first() / t.second())
                .collect(VectorCollector.toVector());
    }

    @Override
    public double sum() {
        return features.stream().mapToDouble(f -> f).sum();
    }

    @Override
    public Vector<Double> divide(double value) {
        if (value == 0) {
            log.warning("Division by zero is not allowed");
            throw new VectorException("value cannot be zero");
        }
        return features.stream().map(f -> f / value).collect(VectorCollector.toVector());
    }

    @Override
    public Vector<Double> toDouble() {
        return this;
    }

    @Override
    public Vector<Double> abs() {
        return features.stream().map(Math::abs).collect(VectorCollector.toVector());
    }

    @Override
    public Vector<Double> add(Vector<Double> anotherVector, Double padding) {
        var difference = size() - anotherVector.size();
        if (difference == 0) {
            return this.add(anotherVector);
        }
        if (difference > 0) {
            DoubleVector padded = new DoubleVector(ListUtils.padding(((DoubleVector) anotherVector).features, padding, Math.abs(difference)));
            return this.add(padded);
        }
        return new DoubleVector(ListUtils.padding(features, padding, Math.abs(difference))).add(anotherVector);
    }


}
