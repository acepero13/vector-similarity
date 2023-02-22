package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;

public enum RACE implements CategoricalFeature<RACE> {
    CAUCASIAN, BLACK, ASIAN;

    @Override
    public RACE originalValue() {
        return this;
    }

    @Override
    public String featureName() {
        return "race";
    }
}
