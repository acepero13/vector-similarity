package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.exceptions.ArgumentException;
import com.acepero13.research.profilesimilarity.testmodels.User;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VectorizableProxyTest {

    private final Vectorizable actual = VectorizableProxy.of(User.defaultUser());

    @Test
    void createVectorizable() {
        assertThat(actual, is(notNullValue()));
    }

    @Test
    void createsListOfFeatures() {
        assertThat(actual.features().size(), equalTo(7));
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

    @Test
    void reducedSetOfEncodingAllTagsAreInValues() {
        var sut = VectorizableProxy.of(new PersonTestWithoutValues(33, List.of(TAG.SPORT, TAG.FAMILY)));
        List<CategoricalFeature<?>> categorical = sut.toFeatureVector().categorical();

        assertThat(categorical.get(0).originalValue(), equalTo(true));
        assertThat(categorical.get(1).originalValue(), equalTo(true));
        assertThat(categorical.size(), equalTo(8));
    }

    @Test
    void actualListHasMoreValuesThanAppected() {
        var sut = VectorizableProxy.of(new PersonTest(33, List.of(TAG.SPORT, TAG.FAMILY, TAG.EMAIL)));
        List<CategoricalFeature<?>> categorical = sut.toFeatureVector().categorical();

        assertThat(categorical.size(), equalTo(3));
    }

    @Test
    void equals() {
        var default2 = VectorizableProxy.of(User.defaultUser());
        assertThat(actual.equals(default2), equalTo(true));
    }

    @Test void classWithoutVectorizableAnnotationRaisesException(){
        var t = assertThrows(ArgumentException.class, () -> VectorizableProxy.of(new ClassWithoutAnnotation(10, new ArrayList<>())));
        assertThat(t.getMessage(), containsString("Missing annotation. Please, make sure to add the @Vectorizable annotation to ClassWithoutAnnotation"));
    }

    @Test
    void twoDifferentObjectsAreDifferent() {

        assertThat(actual, not(equalTo("")));
    }

    private enum TAG {
        SPORT, FAMILY, ECO, SAFETY, MUSIC, EVENTS, READING, EMAIL
    }

    @Data
    private static class ClassWithoutAnnotation {
        @Numerical
        private final int age;
        @Categorical(name = "my-tags", oneHotEncoding = true, enumClass = TAG.class, values = {"SPORT", "FAMILY", "ECO"})
        private final List<TAG> tags;

    }

    @com.acepero13.research.profilesimilarity.annotations.Vectorizable
    @Data
    private static class PersonTest {
        @Numerical
        private final int age;
        @Categorical(name = "my-tags", oneHotEncoding = true, enumClass = TAG.class, values = {"SPORT", "FAMILY", "ECO"})
        private final List<TAG> tags;

    }

    @com.acepero13.research.profilesimilarity.annotations.Vectorizable
    @Data
    private static class PersonTestWithoutValues {
        @Numerical
        private final int age;
        @Categorical(name = "my-tags", oneHotEncoding = true)
        private final List<TAG> tags;

    }


}
