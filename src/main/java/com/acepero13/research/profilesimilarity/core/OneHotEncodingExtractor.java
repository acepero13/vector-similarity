package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.proxy.CategoricalFeatureProxy;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Utility class for one-hot encoding a set of categorical features.
 *
 * @param <T> the type of the categorical feature
 */
public class OneHotEncodingExtractor<T extends CategoricalFeature<?>> {

    /**
     * The prefix for the feature name in the resulting one-hot encoding.
     */
    public static final String ONE_HOT_PREFIX = "one_hot_";
    /**
     * The set of feature names to encode.
     */
    private final Set<T> featureNames;

    /**
     * Constructs a OneHotEncodingExtractor with the given set of feature names.
     *
     * @param featureNames the set of feature names to encode
     */
    public OneHotEncodingExtractor(Set<T> featureNames) {
        this.featureNames = requireNonNull(featureNames);
    }

    /**
     * Constructs a OneHotEncodingExtractor with the given list of feature values.
     *
     * @param elements the list of feature values to encode
     * @param <T>      the type of the categorical feature
     * @return a OneHotEncodingExtractor instance for the given list of feature values
     */
    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(List<T> elements) {
        Set<T> map = new LinkedHashSet<>(requireNonNull(elements));
        return new OneHotEncodingExtractor<>(map);

    }

    /**
     * Constructs a OneHotEncodingExtractor with the given array of feature values.
     *
     * @param values the array of feature values to encode
     * @param <T>    the type of the categorical feature
     * @return a OneHotEncodingExtractor instance for the given array of feature values
     */
    @SafeVarargs
    public static <T extends CategoricalFeature<?>> OneHotEncodingExtractor<T> oneHotEncodingOf(T... values) {
        return oneHotEncodingOf(List.of(requireNonNull(values)));
    }

    /**
     * Returns a list of CategoricalFeature objects for all possible values of the given categorical annotation.
     *
     * @param annotation the categorical annotation
     * @param constants  the possible values of the categorical annotation
     * @param isTarget
     * @return a list of CategoricalFeature objects for all possible values of the given categorical annotation
     */

    public static List<CategoricalFeature<?>> allValuesForOneHot(Categorical annotation, Object[] constants, boolean isTarget) {

        List<CategoricalFeature<?>> features = new ArrayList<>();
        for (Object value : constants) {
            if (value == null) {
                continue;
            }
            CategoricalFeature<?> feature = CategoricalFeatureProxy.of(requireNonNull(value), annotation.name(), isTarget);
            features.add(feature);
        }
        return features;
    }

    /**
     * Converts a list of categorical features into a list of one-hot encoded features.
     *
     * @param elements the list of categorical features to encode
     * @return a list of one-hot encoded features
     */
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

    /**
     * Gets the name of the feature in the one-hot encoding format.
     *
     * @param element the categorical feature
     * @return the name of the feature in the one-hot encoding format
     */
    private String getName(T element) {
        return ONE_HOT_PREFIX + element.featureName();
    }


    /**
     * Converts a list of CategoricalFeature objects into a list of Feature objects that represent the same data as
     * one-hot vectors.
     *
     * @param elements a list of CategoricalFeature objects to convert into one-hot vectors.
     * @return a list of Feature objects that represent the same data as one-hot vectors.
     */
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

