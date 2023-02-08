package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedSimilarity;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class DataSet {
    private final List<Vectorizable> dataPoints;
    private final Similarity similarityScorer = new CombinedSimilarity();


    public DataSet(Vectorizable... vectorizables) {
        this.dataPoints = List.of(vectorizables);
    }


    public Vectorizable mostSimilarTo(Vectorizable target) {
        Normalizer normalizer = buildNormalizerFrom(target);
        Vector<Double> weights = target.features().stream()
                                       .map(Feature::weight)
                                       .collect(VectorCollector.toVector());

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(), normalizer);


        var mostSimilar = this.dataPoints.stream()
                                         .map(v -> Tuple.of(v, v.vector(target.features())))
                                         .map(t -> t.mapSecond(normalizer::normalize))
                                         .map(t -> t.mapSecond(weights::multiply))
                                         .map(t -> t.mapSecond(NormalizedVector::of))
                                         .map(t -> t.mapSecond(v -> calculateScore(normalizedTarget, v)))
                                         .max(Comparator.comparingDouble(Tuple::second))
                                         .orElseThrow();

        return mostSimilar.first();
    }

    private Double calculateScore(NormalizedVector normalizedTarget, NormalizedVector v) {
        return similarityScorer.similarityScore(normalizedTarget, v);
    }

    private Normalizer buildNormalizerFrom(Vectorizable target) {
        List<Vector<Double>> dataSet = getDataPointsWithFeatureSet(target.features());
        return Matrix.buildMinMaxNormalizerFrom(Matrix.ofVectors(dataSet));
    }

    private List<Vector<Double>> getDataPointsWithFeatureSet(List<Feature<?>> features) {
        return dataPoints.stream()
                         .map(d -> d.vector(features))
                         .collect(Collectors.toList());
    }


}
