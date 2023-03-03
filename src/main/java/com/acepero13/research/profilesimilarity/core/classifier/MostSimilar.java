package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.MixedSample;
import com.acepero13.research.profilesimilarity.core.classifier.result.Classification;
import com.acepero13.research.profilesimilarity.core.classifier.result.Prediction;
import com.acepero13.research.profilesimilarity.core.classifier.result.Probability;
import com.acepero13.research.profilesimilarity.core.classifier.result.Result;
import com.acepero13.research.profilesimilarity.core.proxy.VectorizableProxy;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.exceptions.KnnException;
import com.acepero13.research.profilesimilarity.scores.Metrics;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.Data;
import lombok.experimental.Accessors;

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

    private MostSimilar(Vectorizable... vectorizables) {
        this(Metrics.cosineSimilarity(), List.of(vectorizables));
    }


    private MostSimilar(Metric metric, List<Vectorizable> vectorizables) {
        this.dataSet = new DataSet(requireNonNull(vectorizables));
        var featureVector = vectorizables.stream().map(Vectorizable::toFeatureVector).collect(Collectors.toList());
        this.categoricalDataSet = featureVector.stream()

                .map(FeatureVector::categorical)
                .collect(Collectors.toList());
        this.metric = metric;

    }

    public static MostSimilar withDefaultMetric(Vectorizable... vectorizables) {
        return new MostSimilar(vectorizables);
    }

    public static MostSimilar withDefaultMetric(Object... vectorizables) {
        return new MostSimilar(Metrics.cosineSimilarity(), VectorizableProxy.of(List.of(vectorizables)));
    }


    public static MostSimilar of(Metric metric, List<Vectorizable> vectorizables) {
        return new MostSimilar(metric, vectorizables);
    }

    public static MostSimilar of(Metric metric, Vectorizable... vectorizables) {
        return new MostSimilar(metric, List.of(vectorizables));
    }

    public static <T> MostSimilar ofObjects(Metric metric, List<T> vectorizables) {
        return new MostSimilar(metric, VectorizableProxy.of(vectorizables));
    }


    public Vectorizable mostSimilarTo(Vectorizable target) {
        Optional<SimilarScore> finalResult = highestSimilarScore(target);


        return finalResult.map(SimilarScore::sample).orElseThrow();

    }

    public Vectorizable mostSimilarTo(Object target) {
        return mostSimilarTo(VectorizableProxy.of(target));
    }

    public Result resultOfMostSimilarTo(Object target) {
        return highestSimilarScore(VectorizableProxy.of(target)).map(MostSimilarResult::new).orElseThrow();
    }

    public Result resultOfMostSimilarTo(Vectorizable target) {
        return highestSimilarScore(target).map(MostSimilarResult::new).orElseThrow();
    }

    public <T> Optional<T> mostSimilarTo(Object target, Class<T> type) {
        Vectorizable result = mostSimilarTo(VectorizableProxy.of(target));
        return VectorizableProxy.targetOf(result, type);

    }

    private Optional<SimilarScore> highestSimilarScore(Vectorizable target) {
        requireNonNull(target);
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);

        if (normalizedDataSet.isEmpty()) {
            normalizedDataSet = dataSet.scaleAndScore(target, normalizer);
        }

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(target.numericalFeatures()), normalizer);


        var categoricalTarget = target.toFeatureVector().categorical();


        return ListUtils.zip(normalizedDataSet, categoricalDataSet)
                .parallel()
                .map(t -> new NormalizedSample(t, metric))
                .map(s -> s.score(normalizedTarget, categoricalTarget))
                .max(Comparator.comparingDouble(SimilarScore::score));
    }


    private static class MostSimilarResult implements Result {

        private final SimilarScore mostSimilar;

        private MostSimilarResult(SimilarScore mostSimilar) {
            this.mostSimilar = mostSimilar;
        }

        @Override
        public CategoricalFeature<?> classify(String featureName) {
            return mostSimilar.sample.toFeatureVector().getCategoricalFeatureBy(featureName)
                    .orElseThrow(() -> new KnnException("Could not find a suitable category for: " + featureName));
        }

        @Override
        public CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type) {
            return mostSimilar.sample.toFeatureVector().getCategoricalFeatureBy(type)
                    .orElseThrow(() -> new KnnException("Could not find a suitable category for: " + type));
        }

        @Override
        public Classification classifyWithScore(Class<? extends CategoricalFeature<?>> type) {
            CategoricalFeature<?> classification = classify(type);
            return new Classification(classification, Probability.of(mostSimilar.score));
        }

        @Override
        public Double predict(String featureName) {
            return mostSimilar.sample.toFeatureVector().getNumericalFeatureBy(featureName)
                    .map(Feature::featureValue)
                    .orElseThrow(() -> new KnnException("Could not find a suitable category for: " + featureName));
        }

        @Override
        public Prediction predictWithScore(String featureName) {
            double prediction = predict(featureName);

            return new Prediction(prediction, mostSimilar.score * 100); // TODO: To rethink this part
        }
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

        public SimilarScore score(NormalizedVector normalizedTarget, List<CategoricalFeature<?>> categoricalTarget) {

            var sample = MixedSample.of(vector.second(), categorical);
            var another = MixedSample.of(normalizedTarget, categoricalTarget);
            return new SimilarScore(metric.similarityScore(sample, another), vector.first());
        }
    }

    @Data
    @Accessors(fluent = true)
    private static class SimilarScore {
        private final double score;
        private final Vectorizable sample;
    }


}
