package com.acepero13.research.profilesimilarity.core.vectors;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.stream.Stream;

/**
 * A {@link Vector} implementation that represents a collection of normalized double values.
 */
@EqualsAndHashCode
@ToString
public class NormalizedVector implements Vector<Double> {
    private final Vector<Double> vector;

    private NormalizedVector(Vector<Double> original, Normalizer normalizer) {
        this.vector = normalizer.normalize(original);
    }

    private NormalizedVector(Vector<Double> original) {
        this.vector = original;
    }

    /**
     * Creates a new {@link NormalizedVector} object from the specified {@link Vector} of double values and {@link Normalizer}.
     *
     * @param vector     the vector of double values to create the {@link NormalizedVector} object from.
     * @param normalizer the {@link Normalizer} object to be used to normalize the vector.
     * @return a new {@link NormalizedVector} object initialized with the specified vector and normalizer.
     */
    public static NormalizedVector of(Vector<Double> vector, Normalizer normalizer) {
        return new NormalizedVector(vector, normalizer);
    }

    /**
     * Creates a new {@link NormalizedVector} object from the specified {@link Vector} of double values, using the default {@link Normalizer}.
     *
     * @param vector the vector of double values to create the {@link NormalizedVector} object from.
     * @return a new {@link NormalizedVector} object initialized with the specified vector and default {@link Normalizer}.
     */
    public static NormalizedVector of(Vector<Double> vector) {
        return new NormalizedVector(vector);
    }

    @Override
    public Double norm() {
        return vector.norm();
    }

    @Override
    public Double cosine(Vector<Double> another) throws VectorException {
        return vector.cosine(another);
    }

    @Override
    public Double dot(Vector<Double> another) throws VectorException {
        return vector.dot(another);
    }

    @Override
    public Double distanceTo(Vector<Double> another) throws VectorException {
        return vector.distanceTo(another);
    }

    @Override
    public Vector<Double> add(Vector<Double> another) throws VectorException {
        return vector.add(another);
    }

    @Override
    public Vector<Double> subtract(Vector<Double> another) throws VectorException {
        return vector.subtract(another);
    }

    @Override
    public void checkSizeMatchWith(Vector<Double> another) throws VectorException {
        vector.checkSizeMatchWith(another);
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<Double> another) {
        return vector.zip(another);
    }

    @Override
    public Vector<Double> multiply(Vector<Double> another) throws VectorException {
        return vector.multiply(another);
    }

    @Override
    public Double getFeature(int index) {
        return vector.getFeature(index);
    }

    @Override
    public int size() {
        return vector.size();
    }

    @Override
    public MinMax minMax() {
        return vector.minMax();
    }


    @Override
    public Vector<Double> divide(Vector<Double> another) {
        return vector.divide(another);
    }

    @Override
    public double sum() {
        return vector.sum();
    }

    @Override
    public Vector<Double> divide(double value) {
        return vector.divide(value);
    }

    @Override
    public Vector<Double> toDouble() {
        return vector.toDouble();
    }

    @Override
    public Vector<Double> abs() {
        return vector.abs();
    }

    @Override
    public Vector<Double> add(Vector<Double> anotherVector, Double padding) {
        return vector.add(anotherVector, padding);
    }


}
