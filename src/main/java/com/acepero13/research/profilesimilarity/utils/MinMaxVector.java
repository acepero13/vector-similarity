package com.acepero13.research.profilesimilarity.utils;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.core.Matrix;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


/**
 * This class represents a vector containing the minimum and maximum values of a collection of vectors or a matrix. It provides
 * <p>
 * methods to calculate the difference between the minimum and maximum values of each element in the vector.
 */
@Data
public class MinMaxVector {

    /**
     * The minimum values of each element in the vector.
     */
    private final Vector<Double> min;
    /**
     * The maximum values of each element in the vector.
     */
    private final Vector<Double> max;

    /**
     * Creates a new instance of {@link MinMaxVector} with the given minimum and maximum values.
     *
     * @param min The minimum values of each element in the vector.
     * @param max The maximum values of each element in the vector.
     */
    public MinMaxVector(Vector<Double> min, Vector<Double> max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Creates a new instance of {@link MinMaxVector} from a list of {@link MinMax} objects.
     *
     * @param minMaxes A list of {@link MinMax} objects.
     * @return A new instance of {@link MinMaxVector} with the minimum and maximum values extracted from the list of {@link MinMax} objects.
     */
    public static MinMaxVector of(List<MinMax> minMaxes) {
        List<Tuple<Double, Double>> values = minMaxes.stream()
                .map(m -> Tuple.of(m.min(), m.max()))
                .collect(Collectors.toList());

        Vector<Double> min = values.stream()
                .map(Tuple::first)
                .collect(VectorCollector.toVector());

        Vector<Double> max = values.stream()
                .map(Tuple::second)
                .collect(VectorCollector.toVector());

        return new MinMaxVector(min, max);
    }

    /**
     * Creates a new instance of {@link MinMaxVector} from a matrix of doubles.
     *
     * @param matrix A matrix of doubles.
     * @return A new instance of {@link MinMaxVector} with the minimum and maximum values extracted from the matrix of doubles.
     */
    public static MinMaxVector of(Matrix<Double> matrix) {
        return of(matrix.reduceColumnWise(Vector::minMax));
    }

    /**
     * Calculates the difference between the minimum and maximum values of each element in the vector.
     *
     * @return A new vector containing the differences between the minimum and maximum values of each element.
     */
    public Vector<Double> difference() {
        return max.subtract(min);
    }
}