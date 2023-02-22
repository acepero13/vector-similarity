package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Vector<T extends Number> {
    @SuppressWarnings("unchecked")
    static <T extends Number> Vector<T> of(List<T> features) {
        if (features.stream().allMatch(Double.class::isInstance)) {
            return (Vector<T>) DoubleVector.of(features.stream()
                    .map(Double.class::cast).collect(Collectors.toList()));
        }
        return (Vector<T>) DoubleVector.of(new ArrayList<>());
    }

    Double norm();

    Double cosine(Vector<T> another) throws VectorException;

    Double dot(Vector<T> another) throws VectorException;

    Double distanceTo(Vector<T> another) throws VectorException;

    Vector<T> add(Vector<T> another) throws VectorException;

    Vector<T> subtract(Vector<T> another) throws VectorException;

    void checkSizeMatchWith(Vector<T> another) throws VectorException;

    Stream<Tuple<Double, Double>> zip(Vector<T> another);

    Vector<T> multiply(Vector<T> another) throws VectorException;

    T getFeature(int index);

    int size();

    MinMax minMax();


    Vector<Double> divide(Vector<Double> difference);

    double sum();

    Vector<T> divide(double value);

    Vector<Double> toDouble();

    Vector<T> abs();

    Vector<T> add(Vector<T> anotherVector, T padding);
}
