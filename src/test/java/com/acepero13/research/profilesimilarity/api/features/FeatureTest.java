package com.acepero13.research.profilesimilarity.api.features;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class FeatureTest {

    @Test
    void intFeature() {
        Feature<Integer> feat = Features.integerFeature(100, "feat");
        assertThat(feat.featureValue(), closeTo(100.0, 0.01));
        assertThat(feat.originalValue(), equalTo(100));
        assertThat(feat.featureName(), equalTo("feat"));

    }

    @Test
    void booleanFeatureOfTrue() {
        Feature<Boolean> feat = Features.booleanFeature(true, "feat");
        assertThat(feat.featureValue(), closeTo(1.0, 0.01));
        assertThat(feat.originalValue(), equalTo(true));
        assertThat(feat.featureName(), equalTo("feat"));
    }
}