package com.acepero13.research.profilesimilarity.api.features;

import com.acepero13.research.profilesimilarity.api.Feature;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class IntegerFeature implements Feature<Integer> {

    private final int value;
    private final String name;

    public IntegerFeature(int value, String featureName) {
        this.value = value;
        this.name = featureName;
    }


    public static Feature<Integer> of(int value, String featureName) {
        return new IntegerFeature(value, featureName);
    }

    @Override
    public double featureValue() {
        return value;
    }

    @Override
    public Integer originalValue() {
        return value;
    }

    @Override
    public String name() {
        return this.name;
    }
}
