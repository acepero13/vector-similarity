package com.acepero13.research.profilesimilarity.api.features;


import java.util.List;

/**
 * This interface represents a feature in a machine learning model.
 *
 * @param <T> the type of the original value of the feature
 */
public interface Feature<T> {

    /**
     * Returns the numerical value of the feature.
     *
     * @return the numerical value of the feature.
     */
    double featureValue();

    /**
     * Returns the original value of the feature.
     *
     * @return the original value of the feature.
     */
    T originalValue();

    /**
     * Returns the name of the feature.
     *
     * @return the name of the feature.
     */
    String featureName();

    /**
     * Returns the weight of the feature.
     *
     * @return the weight of the feature.
     */
    default double weight() {
        return 1.0;
    }

    /**
     * Returns true if the feature is included in the provided whitelist of features.
     *
     * @param whiteList the whitelist of features to check against.
     * @return true if the feature is included in the whitelist, false otherwise.
     */
    default boolean isWhiteListed(List<Feature<?>> whiteList) {
        return whiteList.stream()
                .map(Feature::featureName)
                .anyMatch(n -> n.equals(featureName()));
    }
}


