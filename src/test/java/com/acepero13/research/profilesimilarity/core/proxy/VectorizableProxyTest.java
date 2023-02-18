package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.testmodels.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class VectorizableProxyTest {

    private final Vectorizable actual = VectorizableProxy.of(User.defaultUser());

    @Test
    void createVectorizable() {
        assertThat(actual, is(notNullValue()));
    }

    @Test
    void createsListOfFeatures() {
        assertThat(actual.features().size(), equalTo(5));
    }

    @Test
    void createsVector() {
        assertThat(actual.toFeatureVector().toDouble(), equalTo(DoubleVector.of(35.0, 70_000.0, 174.0)));
    }

    @Test
    void numericalFeatures() {
        assertThat(actual.numericalFeatures().size(), equalTo(3));
    }

    @Test
    void vectorWithAllFeaturesCausesExceptionBecauseCategorical() {
        assertThrows(UnsupportedOperationException.class, actual::vector);
    }

    @Test
    void vectorFilteringFeatures() {
        List<Feature<?>> features = List.of(Features.integerFeature(35, "age"), Features.integerFeature(70_000, "income"));
        assertThat(actual.vector(features), equalTo(DoubleVector.of(35.0, 70_000.0)));
    }


}