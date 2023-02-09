package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: If they are not the same, better is composition instead of inheretance

public abstract class AbstractClassifier {

    private final Metric metric;
    private final List<Vectorizable> dataPoints;

    protected AbstractClassifier(Metric metric, Vectorizable... vectorizables) {
        this.metric = metric;
        this.dataPoints = List.of(vectorizables);
    }

    protected List<Vectorizable> dataPoints() {
        return dataPoints;
    }

    protected static Normalizer minMaxNormalizer(Vectorizable target, List<Vectorizable> dataPoints) {
        List<Vector<Double>> dataSet = dataPoints.stream()
                .map(d -> d.vector(target.features()))
                .collect(Collectors.toList());
        return Matrix.buildMinMaxNormalizerFrom(Matrix.ofVectors(dataSet));
    }

    protected Stream<Tuple<Vectorizable, Double>> loadDataUsing(Vectorizable target) {
        Normalizer normalizer = getNormalizer(target);
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

    protected abstract Normalizer getNormalizer(Vectorizable target);

    public abstract Vectorizable mostSimilarTo(Vectorizable target);
}
