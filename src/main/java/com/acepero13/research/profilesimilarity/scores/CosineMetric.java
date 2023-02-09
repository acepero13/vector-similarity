package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;

public class CosineMetric implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {
        return vectorizable.cosine(another);
    }
}
