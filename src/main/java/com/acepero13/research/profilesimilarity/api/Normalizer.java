package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.Vector;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

//TODO: Think about a Decorator instead of a normalizer
public interface Normalizer {


    static Normalizer minMaxNormalizer(Matrix<Double> matrix) {

        List<UnaryOperator<Double>> mapper = matrix.reduceColumnWise(Vector::minMax).stream()
                .map(Matrix::minMaxNormalization)
                .collect(Collectors.toList());
        return vector -> vector.mapEach(mapper);
    }

    Vector<Double> normalize(Vector<Double> vector);
}
