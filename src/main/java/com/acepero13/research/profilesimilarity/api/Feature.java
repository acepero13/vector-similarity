package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.IntegerFeature;

public interface Feature<T> {

    static Feature<Integer> integerFeature(int value, String name) {
        return new IntegerFeature(value, name);
    }

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
