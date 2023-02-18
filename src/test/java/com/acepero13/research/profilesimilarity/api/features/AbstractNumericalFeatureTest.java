package com.acepero13.research.profilesimilarity.api.features;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class AbstractNumericalFeatureTest {
    @Test
    @SuppressWarnings("unchecked")
    void DoubleFeatureIsANumber() {
        //noinspection rawtypes this is just for testing purposes.
        AbstractNumericalFeature<Integer> integerFeat = Features.integerFeature(10, "number");
        assertThat(integerFeat.featureName(), equalTo("number"));
        assertThat(integerFeat.featureValue(), closeTo(10.0, 0.1));
        assertThat(integerFeat.weight(), equalTo(1.0));
        assertThat(integerFeat.originalValue(), equalTo(10));
        assertThat(integerFeat.floatValue(), equalTo(10.0F));
        assertThat(integerFeat.longValue(), equalTo(10L));
        assertThat(integerFeat.intValue(), equalTo(10));
    }

}