package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class EuclideanDistance implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector another) {
        return CalculationUtils.sigmoid(requireNonNull(vector).distanceTo(requireNonNull(another)));
    }
}
