package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedSimilarity;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class DataSet {
    private final List<Vectorizable> dataPoints;
    private final Similarity similarityScorer = new CombinedSimilarity();
    private final Normalizer normalizer;

    public DataSet(Vectorizable... vectorizables) {
        this.dataPoints = List.of(vectorizables);
        this.normalizer = Matrix.buildMinMaxNormalizerFrom(Matrix.of(vectorizables));
    }


    public Vectorizable mostSimilarTo(Vectorizable target) {


        List<Double> weights = target.features().stream().map(Feature::weight).collect(Collectors.toList());
        NormalizedVector normalizedTarget = NormalizedVector.of(normalize(target.vector()).mapEach(applyWeights(weights)));

        // TODO:
        // normalized = target.minus(minVector).dividedBy(differenceVector)

        // weighted = normalized.multiply(weights)



        var mostSimilar = this.dataPoints.stream()
                .map(v -> Tuple.of(v, v.vector(target.features())))
                .map(t -> t.mapSecond(this::normalize))
                .map(t -> t.mapSecond(applyWeightsToVector(weights)))
                .map(t -> t.mapSecond(v -> similarityScorer.similarityScore(normalizedTarget, v)))
                .max(Comparator.comparingDouble(Tuple::second))
                .orElseThrow();

        return mostSimilar.first();
    }

    private static Function<NormalizedVector, NormalizedVector> applyWeightsToVector(List<Double> weights) {
        return v -> NormalizedVector.of(v.mapEach(applyWeights(weights)));
    }

    private static List<UnaryOperator<Double>> applyWeights(List<Double> weights) {
        return weights.stream()
                .map(DataSet::applicativeWeight)
                .collect(Collectors.toList());
    }

    private static UnaryOperator<Double> applicativeWeight(double weight) {
        return v -> v * weight;
    }


    NormalizedVector normalize(Vector<Double> vector) {
        return NormalizedVector.of(vector, normalizer);
    }

}
