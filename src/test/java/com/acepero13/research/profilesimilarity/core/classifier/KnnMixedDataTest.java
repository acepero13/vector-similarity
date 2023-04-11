package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.annotations.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.scores.Metrics;
import com.acepero13.research.profilesimilarity.testmodels.HOBBY;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class KnnMixedDataTest {

    private static final List<AnnotatedPerson> ANNOTATED_SAMPLES = List.of(
            new AnnotatedPerson(30, 30_000, List.of(HOBBY.MUSIC)),
            new AnnotatedPerson(60, 60_000, List.of(HOBBY.MUSIC, HOBBY.SPORT)),
            new AnnotatedPerson(45, 60_000, List.of(HOBBY.SPORT, HOBBY.MUSIC)),
            new AnnotatedPerson(33, 60_000, List.of(HOBBY.MUSIC)),
            new AnnotatedPerson(39, 50_000, List.of(HOBBY.SPORT))
    );

    private static final List<com.acepero13.research.profilesimilarity.api.Vectorizable> PERSON_LIST = List.of(
            new Person(30, 30_000, List.of(HOBBY.MUSIC)),
            new Person(60, 60_000, List.of(HOBBY.MUSIC, HOBBY.SPORT)),
            new Person(45, 60_000, List.of(HOBBY.SPORT, HOBBY.MUSIC)),
            new Person(33, 60_000, List.of(HOBBY.MUSIC)),
            new Person(39, 50_000, List.of(HOBBY.SPORT))
    );

    @Test
    void classifyOneHot() {

        var target = new Person(40, 60_000, new ArrayList<>());

        var knn = KnnMixedData.ofVectorizable(3, PERSON_LIST);
        var result = knn.fit(target).classifyOneHot(n -> n.contains("hobby_"));
        List<HOBBY> hobbies = result.stream()
                .filter(HOBBY::isSet)
                .map(HOBBY::of)
                .collect(Collectors.toList());

        assertThat(hobbies, equalTo(List.of(HOBBY.MUSIC, HOBBY.SPORT)));

    }

    @Test
    void classifyAnnotatedOneHot() {

        var target = new AnnotatedPerson(40, 60_000, new ArrayList<>());

        var knn = KnnMixedData.ofObjects(3, ANNOTATED_SAMPLES);
        var result = knn.fit(target).classifyOneHot(n -> n.contains("hobby_"));
        List<HOBBY> hobbies = result.stream()
                .filter(HOBBY::isSet)
                .map(HOBBY::of)
                .collect(Collectors.toList());

        assertThat(hobbies, equalTo(List.of(HOBBY.MUSIC, HOBBY.SPORT)));

    }

    @Test
    void findMostSimilarProfile() {


        var target = new Person(40, 60_000, new ArrayList<>());

        var classifier = MostSimilar.of(Metrics.gowersMetricCosineAndDice(), PERSON_LIST);
        var actual = classifier.mostSimilarTo(target);

        assertThat(actual, equalTo(PERSON_LIST.get(4)));
    }



    @Test
    void findMostSimilarAnnotatedProfile() {
        var target = new AnnotatedPerson(40, 60_000, new ArrayList<>());

        var classifier = MostSimilar.ofObjects(Metrics.gowersMetricCosineAndDice(), ANNOTATED_SAMPLES);
        var actual = classifier.mostSimilarTo(target);

        assertThat(actual, equalTo(ANNOTATED_SAMPLES.get(4)));
    }

    @Test
    void findMostSimilarAnnotatedProfileWithResult() {
        var target = new AnnotatedPerson(40, 60_000, new ArrayList<>());

        var classifier = MostSimilar.ofObjects(Metrics.gowersMetricCosineAndDice(), ANNOTATED_SAMPLES);
        var result = classifier.resultOfMostSimilarTo(target);
        var prediction = result.predictWithScore("age");

        assertThat(prediction.prediction(), equalTo(39.0));
        assertThat(prediction.score(), closeTo(49.74, 0.1));
    }

    @Test
    void predictAgeFromPerson() {


        var target = new Person(40, 60_000, new ArrayList<>());

        var classifier = MostSimilar.of(Metrics.gowersMetricCosineAndDice(), PERSON_LIST);
        var result = classifier.resultOfMostSimilarTo(target);
        var prediction = result.predictWithScore("age");

        assertThat(prediction.prediction(), equalTo(39.0));
        assertThat(prediction.score(), closeTo(49.74, 0.1));
    }

    @EqualsAndHashCode(callSuper = true)
    private static class Person extends AbstractVectorizable {
        private int age;
        private double income;
        private List<HOBBY> hobbies;

        public Person(int age, double income, List<HOBBY> hobbies) {
            addNonNullFeature(Features.integerFeature(age, "age"))
                    .addNonNullFeature(Features.doubleFeature(income, "income"))
                    .addAsOneHotEncodingFeature(HOBBY.values(), hobbies);
        }


    }

    @Vectorizable
    @Data
    private static class AnnotatedPerson {
        @Numerical
        private final int age;
        @Numerical
        private final double income;
        @Categorical(oneHotEncoding = true, type = HOBBY.class)
        private final List<HOBBY> hobbies;
    }

}