package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;

public class EuclideanDistance implements Similarity {
    @Override
    public Double similarityScore(Vector vectorizable, Vector another) {
        return CalculationUtils.sigmoid(vectorizable.distanceTo(another));
    }
}
