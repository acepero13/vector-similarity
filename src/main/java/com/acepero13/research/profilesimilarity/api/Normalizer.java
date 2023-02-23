package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.MinMaxVector;

/**

 A functional interface representing a normalizer that can normalize a given vector.
 */
public interface Normalizer {

    /**

     Returns a normalizer that normalizes a vector using the min-max normalization technique.
     @param matrix a matrix whose min-max normalization will be used as the normalization range.
     @return a normalizer that uses the min-max normalization technique.
     */
    static Normalizer minMaxNormalizer(Matrix<Double> matrix) {
        var minMax = MinMaxVector.of(matrix);
        return target -> NormalizedVector.of(target.subtract(minMax.getMin()).divide(minMax.difference()));
    }
    /**

     Normalizes the given vector.
     @param vector the vector to be normalized.
     @return a normalized version of the vector.
     */
    NormalizedVector normalize(Vector<Double> vector);
}