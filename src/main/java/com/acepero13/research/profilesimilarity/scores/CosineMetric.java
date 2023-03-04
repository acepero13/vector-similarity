package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.MixedSample;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

final class CosineMetric implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector another) {
        return vector.cosine(another);
    }

    @Override
    public Double similarityScore(MixedSample sample, MixedSample another) {
        double categoricalScore = computeCategoricalScore(sample, another);
        return (sample.getVector().cosine(another.getVector()) + categoricalScore) / 2;
    }

    private static double computeCategoricalScore(MixedSample sample, MixedSample another) {
        int matches = sample.numberOfMatches(another);
        int featureCount = sample.numberOfMatchingFeatures(another);
        return (featureCount == 0)
                ? 0.0
                : (double) matches / featureCount;
    }
}
