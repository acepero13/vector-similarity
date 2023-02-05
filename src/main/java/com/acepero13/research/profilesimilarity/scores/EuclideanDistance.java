package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;

public class EuclideanDistance implements Similarity {
    @Override
    public Double similarityScore(Vectorizable vectorizable, Vectorizable another) {
        return CalculationUtils.sigmoid(vectorizable.vector().distanceTo(another.vector()));
    }
}
