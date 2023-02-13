package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.*;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
final class DataSet {
    private final Metric metric;
    private final List<Vectorizable> dataPoints;

    DataSet(Metric metric, Vectorizable... dataPoints) {
        this(metric, List.of(Objects.requireNonNull(dataPoints)));
    }

    DataSet(Metric metric, List<Vectorizable> dataPoints) {
        this.metric = Objects.requireNonNull(metric, "Metric cannot be null");
        this.dataPoints = Objects.requireNonNull(dataPoints, "data points cannot be null");
    }



    public static Normalizer minMaxNormalizer(Vectorizable target, DataSet dataSet) {
        Objects.requireNonNull(target, "target cannot be null");
        Objects.requireNonNull(dataSet, "Dataset cannot be null");
        List<Vector<Double>> featureReducedDataSet = dataSet.dataPoints.stream()
                .parallel()
                .map(d -> d.vector(target.features()))
                .collect(Collectors.toList());
        return Matrix.buildMinMaxNormalizerFrom(Matrix.ofVectors(featureReducedDataSet));
    }

    Stream<Score> scaleAndScore(Vectorizable target, Normalizer normalizer) {
        log.info("Target is: " + target);
        log.info("Number of samples: " + dataPoints.size());
        Vector<Double> weights = target.features().stream()
                .parallel()
                .map(Feature::weight)
                .collect(VectorCollector.toVector());

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(), normalizer);


        return this.dataPoints.stream()
                .parallel()
                .map(v -> Tuple.of(v, v.vector(target.features())))
                .map(t -> t.mapSecond(normalizer::normalize))
                .map(t -> t.mapSecond(weights::multiply))
                .map(t -> t.mapSecond(NormalizedVector::of))
                .map(t -> t.mapSecond(v -> calculateScore(normalizedTarget, v)))
                .map(t -> new Score(t.second(), t.first()));
    }

    private Double calculateScore(NormalizedVector normalizedTarget, NormalizedVector v) {
        return metric.similarityScore(normalizedTarget, v);
    }



    public static class Score {
        private final double score;
        private final Vectorizable sample;

        public Score(double score, Vectorizable sample) {
            this.score = score;
            this.sample = sample;
        }

        public Vectorizable sample() {
            return sample;
        }

        public double score() {
            return score;
        }
    }
}
