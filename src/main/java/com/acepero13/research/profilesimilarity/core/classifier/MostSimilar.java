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
import com.acepero13.research.profilesimilarity.exceptions.PredictionException;
import com.acepero13.research.profilesimilarity.scores.Metrics;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * MostSimilar is a utility class that computes the most similar vector or object from a list of Vectorizables, using a specified Metric.
 * <p>
 * It provides methods to create an instance with default metric or with specified metric and vectorizables. Additionally, there are methods
 * <p>
 * to compute the most similar Vectorizable, as well as to get the result of most similar Vectorizable, and to get the most similar Vectorizable
 * <p>
 * casted to a specified type.
 */

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
        var featureVector = requireNonNull(vectorizables).stream().map(Vectorizable::toFeatureVector).collect(Collectors.toList());
        this.categoricalDataSet = featureVector.stream()

                .map(FeatureVector::categorical)
                .collect(Collectors.toList());
        this.metric = requireNonNull(metric);

    }

    /**
     * Creates a new MostSimilar instance with default metric.
     *
     * @param vectorizables the list of Vectorizables to be compared
     * @return a new MostSimilar instance with default metric
     */
    public static MostSimilar withDefaultMetric(Vectorizable... vectorizables) {
        return new MostSimilar(vectorizables);
    }

    /**
     * Creates a new MostSimilar instance with default metric.
     *
     * @param vectorizables the list of objects to be compared, which will be converted to Vectorizables using VectorizableProxy
     * @return a new MostSimilar instance with default metric
     */
    public static MostSimilar withDefaultMetric(Object... vectorizables) {
        return new MostSimilar(Metrics.cosineSimilarity(), VectorizableProxy.of(List.of(requireNonNull(vectorizables))));
    }

    /**
     * Creates a new MostSimilar instance with the specified Metric and list of Vectorizables.
     *
     * @param metric        the Metric to be used for the comparison
     * @param vectorizables the list of Vectorizables to be compared
     * @return a new MostSimilar instance with specified metric and vectorizables
     */
    public static MostSimilar of(Metric metric, List<Vectorizable> vectorizables) {
        return new MostSimilar(requireNonNull(metric), requireNonNull(vectorizables));
    }

    /**
     * Creates a new MostSimilar instance with the specified Metric and array of Vectorizables.
     *
     * @param metric        the Metric to be used for the comparison
     * @param vectorizables the array of Vectorizables to be compared
     * @return a new MostSimilar instance with specified metric and vectorizables
     */
    public static MostSimilar of(Metric metric, Vectorizable... vectorizables) {
        return new MostSimilar(requireNonNull(metric), List.of(requireNonNull(vectorizables)));
    }

    /**
     * Creates a new MostSimilar instance with the specified Metric and list of objects, which will be converted to Vectorizables
     * using VectorizableProxy.
     *
     * @param <T>           Type of objects for dataset
     * @param metric        the Metric to be used for the comparison
     * @param vectorizables the list of objects to be compared, which will be converted to Vectorizables using VectorizableProxy
     * @return a new MostSimilar instance with specified metric and vectorizables
     */
    public static <T> MostSimilar ofObjects(Metric metric, List<T> vectorizables) {
        return new MostSimilar(requireNonNull(metric), VectorizableProxy.of(requireNonNull(vectorizables)));
    }

    /**
     * Returns the Vectorizable from the list that is most similar to the target Vectorizable.
     *
     * @param target the target Vectorizable to be compared with
     * @return the Vectorizable from the list that is most similar to the target Vectorizable
     * @throws NoSuchElementException if the list of Vectorizables is empty
     */
    public Vectorizable mostSimilarTo(Vectorizable target) throws NoSuchElementException {
        Optional<SimilarScore> finalResult = highestSimilarScore(requireNonNull(target));


        return finalResult.map(SimilarScore::sample).orElseThrow();

    }

    /**
     * Returns the Vectorizable from the list that is most similar to the target object.
     * The target object will be converted to a Vectorizable using VectorizableProxy.
     *
     * @param target the target object to be compared with
     * @return the Vectorizable from the list that is most similar to the target object
     * @throws NoSuchElementException if the list of Vectorizables is empty
     */
    public Vectorizable mostSimilarTo(Object target) throws NoSuchElementException {
        return mostSimilarTo(VectorizableProxy.of(requireNonNull(target)));
    }

    /**
     * Returns a Result object containing the Vectorizable object from the dataset that is most similar to the given target object.
     * Uses VectorizableProxy to convert the target object into a Vectorizable object and then calls the private method highestSimilarScore.
     * Throws NoSuchElementException if no Vectorizable object in the dataset is similar to the given target.
     *
     * @param target the target object to find the most similar Vectorizable object to
     * @return a Result object containing the most similar Vectorizable object to the given target
     * @throws NoSuchElementException if no Vectorizable object in the dataset is similar to the given target
     */
    public Result resultOfMostSimilarTo(Object target) throws NoSuchElementException {
        return highestSimilarScore(VectorizableProxy.of(requireNonNull(target))).map(MostSimilarResult::new).orElseThrow();
    }

    /**
     * Returns a Result object containing the Vectorizable object from the dataset that is most similar to the given target Vectorizable object.
     * Calls the private method highestSimilarScore with the given target.
     * Throws NoSuchElementException if no Vectorizable object in the dataset is similar to the given target.
     *
     * @param target the target Vectorizable object to find the most similar Vectorizable object to
     * @return a Result object containing the most similar Vectorizable object to the given target
     * @throws NoSuchElementException if no Vectorizable object in the dataset is similar to the given target
     */
    public Result resultOfMostSimilarTo(Vectorizable target) {
        return highestSimilarScore(requireNonNull(target)).map(MostSimilarResult::new).orElseThrow();
    }

    /**
     * Returns the object from the dataset that is most similar to the given target object, and casts it to the specified type.
     *
     * @param target the object to compare against the dataset
     * @param type   the class of the object to cast the result to
     * @param <T>    the type of the object to cast the result to
     * @return an {@code Optional} containing the object from the dataset that is most similar to the target object, or an empty Optional if no such object exists, cast to the specified type
     * @throws NullPointerException if the target object is null
     */
    public <T> Optional<T> mostSimilarTo(Object target, Class<T> type) {
        Vectorizable result = mostSimilarTo(VectorizableProxy.of(Objects.requireNonNull(target)));
        return VectorizableProxy.targetOf(requireNonNull(result), type);

    }

    /**
     * Computes the highest similarity score between the given target vectorizable and the dataset of vectorizables,
     * <p>
     * using the specified metric. Returns an optional containing the highest similarity score if it exists, otherwise empty.
     *
     * @param target the vectorizable to compare against the dataset
     * @return an Optional containing the highest similarity score between the target vectorizable and the dataset, if it exists
     * @throws NullPointerException if target is null
     */

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
                    .orElseThrow(() -> new PredictionException("Could not find a suitable category for: " + featureName));
        }

        @Override
        public CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type) {
            return mostSimilar.sample.toFeatureVector().getCategoricalFeatureBy(type)
                    .orElseThrow(() -> new PredictionException("Could not find a suitable category for: " + type));
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
                    .orElseThrow(() -> new PredictionException("Could not find a suitable category for: " + featureName));
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
