package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.MixedSample;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.scores.CosineMetric;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


public class MostSimilar {

    private final DataSet dataSet;
    private final List<List<CategoricalFeature<?>>> categoricalDataSet;
    private final Metric metric;
    private List<Tuple<Vectorizable, NormalizedVector>> normalizedDataSet = new ArrayList<>();

    public MostSimilar(Vectorizable... vectorizables) {
        this(new CosineMetric(), List.of(vectorizables));
    }


    public MostSimilar(Metric metric, List<Vectorizable> vectorizables) {
        this.dataSet = new DataSet(requireNonNull(vectorizables));
        var featureVector = vectorizables.stream().map(Vectorizable::toFeatureVector).collect(Collectors.toList());
        this.categoricalDataSet = featureVector.stream().parallel().map(FeatureVector::categorical)
                                               .collect(Collectors.toList());
        this.metric = metric;

    }

    public Vectorizable mostSimilarTo(Vectorizable target) {
        requireNonNull(target);
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);

        if (normalizedDataSet.isEmpty()) {
            normalizedDataSet = dataSet.scaleAndScore(target, normalizer);
        }

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(target.numericalFeatures()), normalizer);


        var categoricalTarget = target.toFeatureVector().categorical();


        Optional<DataSet.Score> finalResult = ListUtils.zip(normalizedDataSet, categoricalDataSet)
                                                       .map(t -> new NormalizedSample(t, metric))
                                                       .map(s -> s.score(normalizedTarget, categoricalTarget))
                                                       .max(Comparator.comparingDouble(DataSet.Score::score));


        return finalResult.map(DataSet.Score::sample).orElseThrow();

    }


    private static class NormalizedSample {
        private final Tuple<Vectorizable, NormalizedVector> vector;
        private final List<CategoricalFeature<?>> categorical;
        private final Metric metric;

        public NormalizedSample(Tuple<Tuple<Vectorizable, NormalizedVector>, List<CategoricalFeature<?>>> tuple, Metric metric) {
            this.vector = tuple.first();
            this.categorical = tuple.second();
            this.metric = metric;
        }

        public DataSet.Score score(NormalizedVector normalizedTarget, List<CategoricalFeature<?>> categoricalTarget) {

            var sample = MixedSample.of(vector.second(), categorical);
            var another = MixedSample.of(normalizedTarget, categoricalTarget);
            return new DataSet.Score(metric.similarityScore(sample, another), vector.first());
        }
    }


}
