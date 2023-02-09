package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.*;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
final class DataSet {
    private final Metric metric;
    private final List<Vectorizable> dataPoints;

    DataSet(Metric metric, Vectorizable... vectorizables) {
        this.metric = metric;
        this.dataPoints = List.of(vectorizables);
    }


    public static Normalizer minMaxNormalizer(Vectorizable target, DataSet dataSet) {
        List<Vector<Double>> featureReducedDataSet = dataSet.dataPoints.stream()
                                                                       .map(d -> d.vector(target.features()))
                                                                       .collect(Collectors.toList());
        return Matrix.buildMinMaxNormalizerFrom(Matrix.ofVectors(featureReducedDataSet));
    }

    Stream<Tuple<Vectorizable, Double>> loadDataUsing(Vectorizable target, Normalizer normalizer) {
        Vector<Double> weights = target.features().stream()
                                       .map(Feature::weight)
                                       .collect(VectorCollector.toVector());

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(), normalizer);


        return this.dataPoints.stream()
                              .map(v -> Tuple.of(v, v.vector(target.features())))
                              .map(t -> t.mapSecond(normalizer::normalize))
                              .map(t -> t.mapSecond(weights::multiply))
                              .map(t -> t.mapSecond(NormalizedVector::of))
                              .map(t -> t.mapSecond(v -> calculateScore(normalizedTarget, v)));
    }

    private Double calculateScore(NormalizedVector normalizedTarget, NormalizedVector v) {
        return metric.similarityScore(normalizedTarget, v);
    }
}
