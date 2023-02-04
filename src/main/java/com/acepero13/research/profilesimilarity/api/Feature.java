package com.acepero13.research.profilesimilarity.api;

public interface Feature<T> {

    double featureValue();

    T originalValue();

    static Feature<Boolean> booleanFeature(boolean value) {
        return new Feature<>() {
            @Override
            public double featureValue() {
                return value ? 1.0 : 0.0;
            }

            @Override
            public Boolean originalValue() {
                return value;
            }
        };
    }
}
