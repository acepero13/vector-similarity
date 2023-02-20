package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class CategoricalProxyTest {

    private final com.acepero13.research.profilesimilarity.api.features.CategoricalFeature<Object> feat = CategoricalFeatureProxy.of(GENDER.MALE, "gender");

    @Test void value(){
        assertThat(feat.originalValue(), equalTo(GENDER.MALE));
    }

    @Test void name() {
        assertThat(feat.featureName(), equalTo("gender"));
    }

    @Test void featureValueThrowsException(){
        assertThrows(UnsupportedOperationException.class,  () -> {feat.featureValue();});
    }
    @Test void weight(){
        assertThat(feat.weight(), equalTo(1.0));
    }

    @Test void testToString(){
        assertThat(feat.toString(), equalTo("MALE"));
    }
    @Categorical(name = "gender")
    private enum GENDER {
        MALE, FEMALE
    }
}