package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.MinMaxVector;

public interface Normalizer {


    static Normalizer minMaxNormalizer(Matrix<Double> matrix) {
        var minMax = MinMaxVector.of(matrix);
        return target -> NormalizedVector.of(target.subtract(minMax.getMin()).divide(minMax.difference()));
    }


    NormalizedVector normalize(Vector<Double> vector);
}
