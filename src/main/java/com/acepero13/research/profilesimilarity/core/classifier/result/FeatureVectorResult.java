package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.exceptions.KnnException;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FeatureVectorResult implements KnnResult {
    private final List<FeatureVector> vectors;

    public FeatureVectorResult(List<FeatureVector> vectors) {
        this.vectors = vectors;
    }

    @Override
    public CategoricalFeature<?> classify(String featureName) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(featureName);

        return classify(groups);
    }

    @Override
    public List<CategoricalFeature<?>> classifyOneHot(Predicate<String> featureNameMatcher) {
        // TODO: Refactor this

        var feats = vectors.stream()
                           //.parallel()
                           .flatMap(v -> v.categorical().stream())

                           .map(f -> f.featureName())
                           .filter(featureNameMatcher)
                           .collect(Collectors.toSet());


        List<CategoricalFeature<?>> result = new ArrayList<>();
        for (var feat : feats) {

            var values = vectors.stream()
                                .parallel()
                                .map(v -> Tuple.of(v.getCategoricalFeatureBy(feat), v))
                                .filter(t -> t.first().isPresent())
                                .map(t -> t.mapFirst(Optional::get))
                                .collect(Collectors.toList());

            Map<Object, List<Tuple<? extends CategoricalFeature<?>, FeatureVector>>> countMap = new HashMap<>();
            for (var value : values) {
                var items = countMap.getOrDefault(value.first().originalValue(), new ArrayList<>());
                items.add(value);
                countMap.put(value.first().originalValue(), items);
            }



            var sorted =  countMap.entrySet().stream()
                                    .sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()))
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

            var it = sorted
                    .entrySet()
                    .iterator();
            if(it.hasNext()) {
                                var value = it.next();
                                value.getValue().stream().findFirst().map(v -> v.first()).ifPresent(result::add);
            }
        }

        return result;



    }

    @Override
    public CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(type);

        return classify(groups);
    }

    private static CategoricalFeature<?> classify(Map<CategoricalFeature<?>, Long> groups) {
        List<Map.Entry<CategoricalFeature<?>, Long>> sortedEntries = new ArrayList<>(groups.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        if (sortedEntries.isEmpty()) {
            throw new KnnException("Could not find a suitable category ");
        }
        return sortedEntries.get(0).getKey();
    }

    private Map<CategoricalFeature<?>, Long> groupResultsByCategory(Class<? extends CategoricalFeature<?>> type) {
        return vectors.stream()
                      .parallel()
                      .map(v -> Tuple.of(v.getCategoricalFeatureBy(type), v))
                      .filter(t -> t.first().isPresent())
                      .map(t -> t.mapFirst(Optional::get))
                      .collect(Collectors.groupingBy(Tuple::first, Collectors.counting()));
    }

    private Map<CategoricalFeature<?>, Long> groupResultsByCategory(String featureName) {

        return vectors.stream()
                      .parallel()
                      .map(v -> Tuple.of(v.getCategoricalFeatureBy(featureName), v))
                      .filter(t -> t.first().isPresent())
                      .map(t -> t.mapFirst(Optional::get))
                      .collect(Collectors.groupingBy(Tuple::first, Collectors.counting()));


    }


    @Override
    public Double predict(String featureName) {
        if (vectors.isEmpty()) {
            throw new KnnException("List of vectors is empty");
        }
        return vectors.stream()
                      .parallel()
                      .map(v -> v.getNumericalFeatureBy(featureName))
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .mapToDouble(Feature::featureValue)
                      .sum() / vectors.size();
    }
}
