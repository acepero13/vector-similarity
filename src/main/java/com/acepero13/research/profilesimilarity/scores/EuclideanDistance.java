package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;

public class EuclideanDistance implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector another) {
        return CalculationUtils.sigmoid(vector.distanceTo(another));
    }
}
