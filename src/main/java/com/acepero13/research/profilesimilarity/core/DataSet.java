package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedSimilarity;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.List;
import java.util.stream.Collectors;


public class DataSet {
    private final List<Vectorizable> dataPoints;
    private final Similarity similarityScorer = new CombinedSimilarity();
    private final Normalizer normalizer;

    public DataSet(Vectorizable... vectorizables) {
        this.dataPoints = List.of(vectorizables);
        this.normalizer = Matrix.buildMinMaxNormalizerFrom(Matrix.of(vectorizables));

    }


    public Vectorizable mostSimilarTo(Vectorizable vectorizable) {
        NormalizedVector normalizedSource = normalize(vectorizable);

        List<Tuple<Vectorizable, Double>> mostSimilar = this.dataPoints.stream()
                .map(v -> Tuple.of(v, similarityScorer.similarityScore(normalize(v), normalizedSource)))
                .sorted((f, s) -> Double.compare(s.second(), f.second()))
                .collect(Collectors.toList());

        return mostSimilar.get(0).first();
    }

    NormalizedVector normalize(Vectorizable v) {
        return NormalizedVector.of(v.vector(), normalizer);
    }

}
