package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.exceptions.KnnException;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureVectorResult<T> implements KnnResult<T> {
    private final List<FeatureVector> vectors;

    public FeatureVectorResult(List<FeatureVector> vectors) {
        this.vectors = vectors;
    }

    @Override
    public CategoricalFeature<T> classify(String featureName) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(featureName);

        List<Map.Entry<CategoricalFeature<?>, Long>> sortedEntries = new ArrayList<>(groups.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


        if (sortedEntries.isEmpty()) {
            throw new KnnException("Could not find a suitable category for feature name: " + featureName);
        }
        return (CategoricalFeature<T>) sortedEntries.get(0).getKey();
    }

    //TODO: Remove duplications

    @Override
    public CategoricalFeature<T> classify(Class<? extends CategoricalFeature<?>> type) {
        Map<CategoricalFeature<?>, Long> groups = groupResultsByCategory(type);

        List<Map.Entry<CategoricalFeature<?>, Long>> sortedEntries = new ArrayList<>(groups.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


        if (sortedEntries.isEmpty()) {
            throw new KnnException("Could not find a suitable category for feature name: " + type);
        }
        return (CategoricalFeature<T>) sortedEntries.get(0).getKey();
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
            throw new KnnException("List of vectors is empty");
        }
        return vectors.stream()
                .map(v -> v.getNumericalFeatureBy(featureName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToDouble(Feature::featureValue)
                .sum() / vectors.size();
    }
}
