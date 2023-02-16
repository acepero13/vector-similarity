package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OneHotEncodingExtractor<T extends CategoricalFeature<?>> {

    private final Set<T> featureNames;

    public OneHotEncodingExtractor(Set<T> featureNames) {
        this.featureNames = featureNames;
    }

    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(List<T> elements) {
        Set<T> map = new LinkedHashSet<>(elements);
        return new OneHotEncodingExtractor<>(map);

    }

    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(T... values) {
        return oneHotEncodingOf(List.of(values));
    }

    public List<Feature<?>> convert(List<T> elements) {
        List<Feature<?>> oneHotVectorList = new ArrayList<>();

        for (T element : featureNames) {
            if (elements.stream().anyMatch(e -> e.matches(element))) {
                oneHotVectorList.add(Features.booleanFeature(true, element.featureName()) );
            } else {
                oneHotVectorList.add(Features.booleanFeature(false, element.featureName()));
            }
        }
        return oneHotVectorList;
    }


}
