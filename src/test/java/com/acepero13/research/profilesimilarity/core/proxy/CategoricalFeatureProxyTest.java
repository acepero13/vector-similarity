package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.annotations.CategoricalFeature;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class CategoricalFeatureProxyTest {

    private com.acepero13.research.profilesimilarity.api.features.CategoricalFeature<Object> feat = CategoricalFeatureProxy.of(GENDER.MALE, "gender");

    @Test void value(){
        assertThat(feat.originalValue(), equalTo(GENDER.MALE));
    }

    @Test void name() {
        assertThat(feat.featureName(), equalTo("gender"));
    }

    @Test void featureValueThrowsException(){
        assertThrows(UnsupportedOperationException.class,  () -> {feat.featureValue();});
    }
    @CategoricalFeature(name = "gender")
    private enum GENDER {
        MALE, FEMALE
    }
}