package com.acepero13.research.profilesimilarity.api.features;

import com.acepero13.research.profilesimilarity.api.Feature;
import lombok.Data;

@Data
public class IntegerFeature implements Feature<Integer> {

    private final int value;
    private final String name;

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
