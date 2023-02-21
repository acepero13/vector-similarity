package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;


public enum HOBBY implements CategoricalFeature<HOBBY> {
    READ, SPORT, MUSIC;


    public static HOBBY of(CategoricalFeature<?> str) {
        if (str.featureName().contains(READ.name())) {
            return READ;
        }
        if (str.featureName().contains(SPORT.name())) {
            return SPORT;
        }
        return MUSIC;
    }

    public static boolean isSet(CategoricalFeature<?> f) {
        return (Boolean) f.originalValue();
    }

    @Override
    public HOBBY originalValue() {
        return this;
    }

    @Override
    public String featureName() {
        return "hobby_" + this;
    }
}
