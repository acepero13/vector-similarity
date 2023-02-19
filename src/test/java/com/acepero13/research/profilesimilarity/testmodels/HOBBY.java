package com.acepero13.research.profilesimilarity.testmodels;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import lombok.Data;

// TODO: Case for oneHotEncoding where the enum is not implementing the categoricalFeature

public enum HOBBY implements CategoricalFeature<HOBBY> {
    READ, SPORT, MUSIC;


    public static HOBBY of(CategoricalFeature<?> str) {
        if(str.featureName().contains(READ.name())) {
            return READ;
        }
        if(str.featureName().contains(SPORT.name())) {
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
