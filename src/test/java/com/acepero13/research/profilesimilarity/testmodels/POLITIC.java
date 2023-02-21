package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;

public enum POLITIC implements CategoricalFeature<POLITIC> {
    MODERATE, LIBERAL, CONSERVATIVE;

    @Override
    public POLITIC originalValue() {
        return this;
    }

    @Override
    public String featureName() {
        return "politic";
    }
}
