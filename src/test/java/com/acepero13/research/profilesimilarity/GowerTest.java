package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.scores.GowerMetric;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GowerTest {

    @Test
    void calculateDistance() {
        var person1 = new Person(22, RACE.CAUCASIAN, 3, 0.39, true, POLITIC.MODERATE);
        var person2 = new Person(33, RACE.ASIAN, 1, 0.34, true, POLITIC.LIBERAL);
        var person3 = new Person(52, RACE.CAUCASIAN, 2, 0.51, true, POLITIC.MODERATE);
        var person4 = new Person(46, RACE.BLACK, 3, 0.63, true, POLITIC.CONSERVATIVE);
        var metric = new GowerMetric();

        //var score = metric.similarityScore(person1.vector(), person2.vector());
        var dataset = List.of(
                person1.toFeatureVector(),
                person2.toFeatureVector(),
                person3.toFeatureVector(),
                person4.toFeatureVector());
        var score = metric.similarityScore(dataset, person1.toFeatureVector(), person2.toFeatureVector());

    }

    private static class Person extends AbstractVectorizable {
        public Person(int age, RACE race, int height, double income, boolean isMale, POLITIC politic) {
            addNonNullFeature(Features.integerFeature(age, "age"))
                    .addNonNullFeature(race)
                    .addNonNullFeature(Features.integerFeature(height, "height"))
                    .addNonNullFeature(Features.doubleFeature(income, "income"))
                    .addNonNullFeature(Features.booleanFeature(isMale, "male"))// TODO: change this
                    .addNonNullFeature(politic);
        }


        public FeatureVector toFeatureVector() {
            return new FeatureVector(features());
        }
    }

    private enum RACE implements CategoricalFeature<RACE> {
        CAUCASIAN, BLACK, ASIAN;

        @Override
        public RACE originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "race";
        }
    }

    private enum POLITIC implements CategoricalFeature<POLITIC> {
        MODERATE, LIBERAL, CONSERVATIVE;

        @Override
        public POLITIC originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "politic";
        }
    }
}
