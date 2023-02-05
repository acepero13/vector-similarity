package com.acepero13.research.profilesimilarity.api;

public interface Feature<T> {

    double featureValue();

    T originalValue();

    String name();

    static Feature<Boolean> booleanFeature(boolean value, String name) {
        return new Feature<>() {
            @Override
            public double featureValue() {
                return value ? 1.0 : 0.0;
            }

            @Override
            public Boolean originalValue() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }
}
