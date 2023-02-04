package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@EqualsAndHashCode
@ToString
public class Vector {
    private final List<Double> features;
    private final int size;

    Vector(List<Double> arr) {
        this.features = Objects.requireNonNull(arr, "Features cannot be null");
        this.size = features.size();
    }

    Vector(Double[] arr) {
        this(List.of(arr));
    }

    public static Vector of(Double... features) {
        return new Vector(features);
    }

    public static Vector of(List<Double> features) {
        return new Vector(features);
    }

    public static Vector of(Feature<?>... features) {
        return new Vector(Stream.of(features)
                .map(Feature::featureValue)
                .collect(Collectors.toList()));
    }

    public Double norm() {
        return Math.sqrt(
                features.stream()
                        .mapToDouble(f -> Math.pow(f, 2))
                        .sum()
        );
    }

    public Double cosine(Vector another) throws VectorException {
        Double denominator = norm() * another.norm();
        if (denominator == 0) {
            throw new VectorException("Error: Norm cannot be zero");
        }
        return dot(another) / denominator;
    }

    public Double dot(Vector another) throws VectorException {
        checkSizeMatchWith(another);
        return zip(another)
                .map(t -> t.first() * t.second())
                .reduce(0.0, Double::sum);
    }

    public Double distanceTo(Vector another) throws VectorException {
        return subtract(another).norm();
    }

    public Vector add(Vector another) throws VectorException {
        checkSizeMatchWith(another);

        return new Vector(zip(another)
                .map(t -> t.first() + t.second())
                .collect(Collectors.toList())
        );
    }

    public Vector subtract(Vector another) throws VectorException {
        checkSizeMatchWith(another);
        return new Vector(zip(another)
                .map(t -> t.first() - t.second())
                .collect(Collectors.toList()));
    }

    private void checkSizeMatchWith(Vector another) {
        if (size != another.size) {
            throw new VectorException("Vector length do not match. Vector length is: " + size + " and the other vector's length is: " + another.size);
        }
    }

    private Stream<Tuple<Double, Double>> zip(Vector another) {
        return IntStream.range(0, size)
                .mapToObj(i -> Tuple.of(features.get(i), another.features.get(i)));
    }

    public Vector multiply(Vector another) throws VectorException {
        checkSizeMatchWith(another);
        return new Vector(zip(another)
                .map(t -> t.first() * t.second())
                .collect(Collectors.toList()));
    }


}
