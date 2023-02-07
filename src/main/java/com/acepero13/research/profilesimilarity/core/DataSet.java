package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedSimilarity;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Comparator;
import java.util.List;


public class DataSet {
    private final List<Vectorizable> dataPoints;
    private final Similarity similarityScorer = new CombinedSimilarity();
    private final Normalizer normalizer;

    public DataSet(Vectorizable... vectorizables) {
        this.dataPoints = List.of(vectorizables);
        this.normalizer = Matrix.buildMinMaxNormalizerFrom(Matrix.of(vectorizables));
    }


    public Vectorizable mostSimilarTo(Vectorizable target) {
        NormalizedVector normalizedTarget = normalize(target.vector());

        var mostSimilar = this.dataPoints.stream()
                .map(v -> Tuple.of(v, v.vector(target.whiteList())))
                .map(t -> t.mapSecond(this::normalize))
                .map(t -> t.mapSecond(v -> similarityScorer.similarityScore(normalizedTarget, v)))
                .max(Comparator.comparingDouble(Tuple::second))
                .orElseThrow();

        return mostSimilar.first();
    }


    NormalizedVector normalize(Vector<Double> vector) {
        return NormalizedVector.of(vector, normalizer);
    }

}
