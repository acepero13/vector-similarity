package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.KnnMixedData;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.testmodels.Gender;
import com.acepero13.research.profilesimilarity.testmodels.POLITIC;
import com.acepero13.research.profilesimilarity.testmodels.RACE;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class GowerTest {



    @Test
    void calculateDistance() {
        var person1 = new Person(22, RACE.CAUCASIAN, 3, 0.39, true, POLITIC.MODERATE);
        var person2 = new Person(33, RACE.ASIAN, 1, 0.34, true, POLITIC.LIBERAL);
        var person3 = new Person(52, RACE.CAUCASIAN, 2, 0.51, true, POLITIC.MODERATE);
        var person4 = new Person(46, RACE.BLACK, 3, 0.63, true, POLITIC.CONSERVATIVE);

        List<FeatureVector> dataset = List.of(
                person1.toFeatureVector(),
                person2.toFeatureVector(),
                person3.toFeatureVector(),
                person4.toFeatureVector());

        KnnMixedData knn = KnnMixedData.of(3, dataset);
        KnnResult result = knn.fit(person2.toFeatureVector());
        CategoricalFeature<?> actualRace = result
                .classify("race");


        assertThat(actualRace, equalTo(RACE.CAUCASIAN));
        Double actualAge = result.predict("age");
        assertThat(actualAge, closeTo(35.6, 0.1));

        CategoricalFeature<?> actualPolitics = result
                .classify(POLITIC.class);

        assertThat(actualPolitics, equalTo(POLITIC.MODERATE));

    }

    private static class Person extends AbstractVectorizable {
        public Person(int age, RACE race, int height, double income, boolean isMale, POLITIC politic) {
            addNonNullFeature(Features.integerFeature(age, "age"))
                    .addNonNullFeature(race)
                    .addNonNullFeature(Features.integerFeature(height, "height"))
                    .addNonNullFeature(Features.doubleFeature(income, "income"))
                    .addNonNullFeature(Gender.from(isMale))
                    .addNonNullFeature(politic);
        }



    }

}
