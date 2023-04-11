package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.Score;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.exceptions.ArgumentException;
import com.acepero13.research.profilesimilarity.exceptions.PredictionException;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.Data;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The confidence score for a KNN regression prediction can be calculated using the following formula:
 * <p>
 * confidence score = 1 / (∑(distance to K nearest neighbors)^p)
 */
final class FeatureVectorResult implements Result {
    private final List<FeatureVector> vectors;
    private final List<Score> scoredVectors;

    public FeatureVectorResult(List<Score> scoredVectors) {
        this.vectors = scoredVectors.stream().map(Score::sample).collect(Collectors.toList());
        this.scoredVectors = scoredVectors;

    }

    private Classification classify(Map<CategoricalFeature<?>, Long> groups) {
        List<Map.Entry<CategoricalFeature<?>, Long>> sortedEntries = new ArrayList<>(groups.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        if (sortedEntries.isEmpty()) {
            throw new PredictionException("Could not find a suitable category ");
        }
        Map.Entry<CategoricalFeature<?>, Long> best = sortedEntries.get(0);
        CategoricalFeature<?> classification = best.getKey();
        double score = best.getValue() / (double) vectors.size();
        return new Classification(classification, Probability.of(score));
    }

    @Override
    public CategoricalFeature<?> classify(String featureName) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(featureName);
        return classify(groups).classification();
    }

    public CategoricalFeature<?> classify() {

        return classify(extractFeatureName());
    }

    private String extractFeatureName() {
        return vectors.stream().flatMap(v -> v.getCategorical().stream())
                      .filter(Feature::isTarget)
                      .findFirst()
                      .map(Feature::featureName)
                      .orElseThrow(() -> new ArgumentException("Could not find a suitable target feature. Check if you added the target argument in the @Categorical annotation. Or implement the boolean isTarget() method is implmented"));
    }

    @Override
    public List<CategoricalFeature<?>> classifyOneHot(Predicate<String> featureNameMatcher) {
        return new OneHotEncodingClassifier(featureNameMatcher, vectors).classify();
    }

    @Override
    public CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(type);

        return classify(groups).classification();
    }

    @Override
    public Classification classifyWithScore(Class<? extends CategoricalFeature<?>> type) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(type);

        return classify(groups);
    }

    @Override
    public Classification classifyWithScore() {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByName(extractFeatureName());

        return classify(groups);
    }

    private Map<CategoricalFeature<?>, Long> groupResultsByName(String name) {
        return vectors.stream()
                      .map(v -> Tuple.of(v.getCategoricalFeatureBy(name), v))
                      .filter(t -> t.first().isPresent())
                      .map(t -> t.mapFirst(Optional::get))
                      .collect(Collectors.groupingBy(Tuple::first, Collectors.counting()));
    }


    private Map<CategoricalFeature<?>, Long> groupResultsByCategory(Class<? extends CategoricalFeature<?>> type) {
        return vectors.stream()
                      .map(v -> Tuple.of(v.getCategoricalFeatureBy(type), v))
                      .filter(t -> t.first().isPresent())
                      .map(t -> t.mapFirst(Optional::get))
                      .collect(Collectors.groupingBy(Tuple::first, Collectors.counting()));
    }

    private Map<CategoricalFeature<?>, Long> groupResultsByCategory(String featureName) {

        return vectors.stream()
                      .map(v -> Tuple.of(v.getCategoricalFeatureBy(featureName), v))
                      .filter(t -> t.first().isPresent())
                      .map(t -> t.mapFirst(Optional::get))
                      .collect(Collectors.groupingBy(Tuple::first, Collectors.counting()));


    }


    @Override
    public Double predict(String featureName) {
        if (vectors.isEmpty()) {
            throw new PredictionException("List of vectors is empty");
        }
        return vectors.stream()
                      .map(v -> v.getNumericalFeatureBy(featureName))
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .mapToDouble(Feature::featureValue)
                      .sum() / vectors.size();
    }

    @Override
    public Prediction predictWithScore(String featureName) {

        Double value = predict(featureName);

        var sum = scoredVectors.stream()
                               .map(sv -> Math.pow(sv.score(), 2))
                               .mapToDouble(Double::doubleValue)
                               .sum();

        double score = sum == 0 ? 0.0 : (1 / sum);

        return new Prediction(value, score);
    }

    @Data
    private static class OneHotEncodingClassifier {
        final Predicate<String> featureNameMatcher;
        final List<FeatureVector> vectors;

        private static void addMostCommonValueTo(List<CategoricalFeature<?>> result, Map<Object, List<CategoricalFeature<?>>> sorted) {
            sorted.values().stream()
                  .findFirst()
                  .flatMap(l -> l.stream().findFirst())
                  .ifPresent(result::add);
        }

        private static Map<Object, List<CategoricalFeature<?>>> sortValuesByOcurrence(Map<Object, List<CategoricalFeature<?>>> countMap) {
            return countMap.entrySet().stream()
                           .sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()))
                           .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                   (v1, v2) -> v1, LinkedHashMap::new));
        }

        private static Map<Object, List<CategoricalFeature<?>>> groupResultBy(List<CategoricalFeature<?>> values) {
            return values.stream()
                         .collect(Collectors.groupingBy(CategoricalFeature::originalValue, HashMap::new,
                                 Collectors.toList()));
        }

        private List<CategoricalFeature<?>> classify() {
            Set<String> feats = extractMatchingFeatures();

            List<CategoricalFeature<?>> result = new ArrayList<>();
            for (var featureName : feats) {
                List<CategoricalFeature<?>> values = extractCategoricalFeatureFor(featureName);
                Map<Object, List<CategoricalFeature<?>>> countMap = groupResultBy(values);
                Map<Object, List<CategoricalFeature<?>>> sorted = sortValuesByOcurrence(countMap);
                addMostCommonValueTo(result, sorted);
            }

            return result;
        }

        private List<CategoricalFeature<?>> extractCategoricalFeatureFor(String featureName) {
            return vectors.stream()
                          .map(v -> v.getCategoricalFeatureBy(featureName))
                          .flatMap(Optional::stream)
                          .collect(Collectors.toList());
        }


        private Set<String> extractMatchingFeatures() {
            return vectors.stream()
                          .flatMap(v -> v.categorical().stream())
                          .map(Feature::featureName)
                          .filter(featureNameMatcher)
                          .collect(Collectors.toSet());
        }

    }

}
