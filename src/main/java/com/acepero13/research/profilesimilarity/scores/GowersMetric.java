package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.MixedSample;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;


final class GowersMetric implements Metric {

    private final Metric numericalMetric;
    private final Metric categoricalMetric;

    public static Metric forCosine() {
        return new GowersMetric(new CosineMetric(), new StrictDiceSimilarity());
    }

    public GowersMetric(Metric numericalMetric, Metric categoricalMetric) {
        this.numericalMetric = numericalMetric;
        this.categoricalMetric = categoricalMetric;
    }

    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {
        return numericalMetric.similarityScore(vectorizable, another);
    }

    @Override
    public Double similarityScore(MixedSample sample, MixedSample another) {
        double numericalScore = this.numericalMetric.similarityScore(sample.getVector(), another.getVector());
        double categoricalScore = this.categoricalMetric.similarityScore(
                Metric.from(sample.getFeatures()),
                Metric.from(another.getFeatures()));

        return (numericalScore + categoricalScore) / 2;
    }


}
