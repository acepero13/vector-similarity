package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

public final class Metrics {
    private Metrics() {
    }

    public static Metric cosineSimilarity() {
        return new CosineMetric();
    }

    public static Metric jaccardDistance() {
        return new JaccardMetric();
    }

    public static Metric normalizedEuclideanDistance() {
        return new EuclideanDistance();
    }

    public static Metric euclideanDistance() {
        return NormalizedVector::distanceTo;
    }

    public static Metric gowersMetricCosineAndDice() {
        return new GowersMetric(new CosineMetric(), new StrictDiceSimilarity());
    }

    public static Metric gowersMetric(Metric numericalMetric, Metric categoricalMetric) {
        return new GowersMetric(numericalMetric, categoricalMetric);
    }
}
