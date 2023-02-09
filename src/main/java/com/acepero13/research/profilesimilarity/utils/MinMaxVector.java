package com.acepero13.research.profilesimilarity.utils;

import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.api.Vector;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
@Data
public class MinMaxVector {
    private final Vector<Double> min;
    private final Vector<Double> max;

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

    public static MinMaxVector of(Matrix<Double> matrix) {
        return of(matrix.reduceColumnWise(Vector::minMax));
    }

    public Vector<Double> difference() {
        return max.subtract(min);
    }
}
