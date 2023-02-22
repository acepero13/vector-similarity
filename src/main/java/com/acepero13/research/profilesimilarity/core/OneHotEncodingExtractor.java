package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.proxy.CategoricalFeatureProxy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OneHotEncodingExtractor<T extends CategoricalFeature<?>> {

    public static final String ONE_HOT_PREFIX = "one_hot_";
    private final Set<T> featureNames;

    public OneHotEncodingExtractor(Set<T> featureNames) {
        this.featureNames = featureNames;
    }

    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(List<T> elements) {
        Set<T> map = new LinkedHashSet<>(elements);
        return new OneHotEncodingExtractor<>(map);

    }

    @SafeVarargs
    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(T... values) {
        return oneHotEncodingOf(List.of(values));
    }

    public static List<CategoricalFeature<?>> allValuesForOneHot(Categorical annotation, Object[] constants) {

        List<CategoricalFeature<?>> features = new ArrayList<>();
        for (Object value : constants) {
            if (value == null) {
                continue;
            }
            CategoricalFeature<?> feature = CategoricalFeatureProxy.of(value, annotation.name());
            features.add(feature);
        }
        return features;
    }

    public List<Feature<?>> convert(List<T> elements) {
        List<Feature<?>> oneHotVectorList = new ArrayList<>();

        for (T element : featureNames) {
            if (elements.stream().anyMatch(e -> e.matches(element))) {
                oneHotVectorList.add(Features.categoricalBoolean(true, getName(element)));
            } else {
                oneHotVectorList.add(Features.categoricalBoolean(false, getName(element)));
            }
        }
        return oneHotVectorList;
    }

    private String getName(T element) {
        return ONE_HOT_PREFIX + element.featureName();
    }


    public List<Feature<?>> convertCategoricalFeature(List<CategoricalFeature<Object>> elements) {
        List<Feature<?>> oneHotVectorList = new ArrayList<>();

        for (T element : featureNames) {
            if (elements.stream().anyMatch(e -> e.matches(element))) {
                oneHotVectorList.add(Features.categoricalBoolean(true, getName(element)));
            } else {
                oneHotVectorList.add(Features.categoricalBoolean(false, getName(element)));
            }
        }
        return oneHotVectorList;
    }
}

