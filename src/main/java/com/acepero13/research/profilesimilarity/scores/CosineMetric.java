package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

public class CosineMetric implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector another) {
        return vector.cosine(another);
    }
}
