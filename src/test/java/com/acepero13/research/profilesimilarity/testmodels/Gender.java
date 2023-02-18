package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;

public enum Gender implements CategoricalFeature<Gender> {
    FEMALE, MALE;

    public static Feature<?> from(boolean isMale) {
        return isMale ? MALE : FEMALE;
    }

    @Override
    public Gender originalValue() {
        return this;
    }

    @Override
    public String featureName() {
        return "gender";
    }
}
