package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.OneHotEncodingExtractor;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.scores.GowersMetric;
import com.acepero13.research.profilesimilarity.testmodels.Gender;
import com.acepero13.research.profilesimilarity.testmodels.HOBBY;
import com.acepero13.research.profilesimilarity.testmodels.POLITIC;
import com.acepero13.research.profilesimilarity.testmodels.RACE;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class KnnMixedDataTest {
    @Test
    void classifyOneHot() {
        var sample1 = new Person(30, 30_000, List.of(HOBBY.MUSIC));
        var sample2 = new Person(60, 60_000, List.of(HOBBY.MUSIC, HOBBY.SPORT));
        var sample3 = new Person(45, 60_000, List.of(HOBBY.SPORT, HOBBY.MUSIC));
        var sample4 = new Person(33, 60_000, List.of(HOBBY.MUSIC));
        var sample5 = new Person(39, 50_000, List.of(HOBBY.SPORT));

        var target = new Person(40, 60_000, new ArrayList<>());

        var knn = KnnMixedData.of(3, List.of(sample1, sample2, sample3, sample4,sample5));
        var result = knn.fit(target).classifyOneHot(n -> n.contains("hobby_"));
        List<HOBBY> hobbies = result.stream()
                .filter(HOBBY::isSet)
                .map(HOBBY::of)
                .collect(Collectors.toList());

        assertThat(hobbies, equalTo(List.of(HOBBY.MUSIC, HOBBY.SPORT)));

    }

    @Test
    void findMostSimilarProfile(){
        var sample1 = new Person(30, 30_000, List.of(HOBBY.MUSIC));
        var sample2 = new Person(60, 60_000, List.of(HOBBY.MUSIC, HOBBY.SPORT));
        var sample3 = new Person(45, 60_000, List.of(HOBBY.SPORT, HOBBY.MUSIC));
        var sample4 = new Person(33, 60_000, List.of(HOBBY.MUSIC));
        var sample5 = new Person(39, 50_000, List.of(HOBBY.SPORT));

        var target = new Person(40, 60_000, new ArrayList<>());

        var classifier = new MostSimilar(GowersMetric.forCosine() , List.of(sample1, sample2, sample3, sample4,sample5));
        var actual = classifier.mostSimilarTo(target);

        assertThat(actual, equalTo(sample3));
    }

    @EqualsAndHashCode(callSuper = false)
    private static class Person extends AbstractVectorizable {
        public Person(int age, double income, List<HOBBY> hobbies) {
            addNonNullFeature(Features.integerFeature(age, "age"))
                    .addNonNullFeature(Features.doubleFeature(income, "income"))
                    .addAsOneHotEncodingFeature(HOBBY.values(), hobbies);
        }


        public FeatureVector toFeatureVector() {
            return new FeatureVector(features());
        }
    }

}