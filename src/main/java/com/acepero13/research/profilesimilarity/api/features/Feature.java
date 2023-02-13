package com.acepero13.research.profilesimilarity.api.features;


import java.util.List;

public interface Feature<T> {


    double featureValue();

    T originalValue();

    String featureName();

    default double weight() {
        return 1.0;
    }


    default boolean isWhiteListed(List<Feature<?>> whiteList) {
        return whiteList.stream()
                .parallel()
                .map(Feature::featureName)
                .anyMatch(n -> n.equals(featureName()));
    }
}
