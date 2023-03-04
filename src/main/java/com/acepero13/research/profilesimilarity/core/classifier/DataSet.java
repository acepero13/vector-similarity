package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
final class DataSet {
    private final List<Vectorizable> dataPoints;

    DataSet(Vectorizable... dataPoints) {
        this(List.of(Objects.requireNonNull(dataPoints)));
    }

    DataSet(List<Vectorizable> dataPoints) {
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

    public static Double calculateScore(Metric metric, NormalizedVector normalizedTarget, NormalizedVector v) {
        return metric.similarityScore(normalizedTarget, v);
    }

    List<Tuple<Vectorizable, NormalizedVector>> scaleAndScore(Vectorizable target, Normalizer normalizer) {
        log.info("Target is: " + target);

        Vector<Double> weights = target.numericalFeatures().stream()
                .map(Feature::weight)
                .collect(VectorCollector.toVector());


        return this.dataPoints.stream()
                .parallel()
                .map(v -> Tuple.of(v, v.vector(target.numericalFeatures())))
                .map(t -> t.mapSecond(normalizer::normalize))
                .map(t -> t.mapSecond(weights::multiply))
                .map(t -> t.mapSecond(NormalizedVector::of))
                .collect(Collectors.toList());
    }

    public int size() {
        return dataPoints.size();
    }


}
