package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;

final class EuclideanDistance implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector another) {
        return CalculationUtils.sigmoid(vector.distanceTo(another));
    }
}
