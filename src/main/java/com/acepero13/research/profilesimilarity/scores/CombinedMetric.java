package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;

import java.util.List;

public class CombinedMetric implements Metric {
    private static final List<Metric> METRIC_SCORERS = List.of(new CosineMetric(), new EuclideanDistance());

    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {

        Double score = METRIC_SCORERS.stream()
                .map(s -> s.similarityScore(vectorizable, another))
                .reduce(0.0, Double::sum);

        return score / (double) METRIC_SCORERS.size();


    }
}
