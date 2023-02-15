package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class KnnRegressionTest {

    public static final String WEIGHT = "weight";

    @Test
    void knnRegression() {
        List<Vectorizable> samples = List.of(
                new User("1", 5, 45, 77),
                new User("2", 5.11, 26, 47),
                new User("3", 5.6, 30, 55),
                new User("4", 5.9, 34, 59),
                new User("5", 4.8, 40, 72),
                new User("6", 5.8, 36, 60),
                new User("7", 5.3, 19, 40),
                new User("8", 5.8, 28, 60),
                new User("9", 5.5, 23, 45),
                new User("10", 5.6, 32, 58)
        );

        User test = new User("11", 5.5, 38, null);

        Knn classifier = new Knn(3, samples);

        var result = classifier.fit(test)
                .predict(WEIGHT);

        assertThat(result, closeTo(57.666666666666664, 0.01));

    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class User extends AbstractVectorizable {
        private final String id;
        private final Double height;
        private final int age;

        private final Integer weight;

        public User(String id, double height, int age, Integer weight) {
            this.id = id;
            this.height = height;
            this.age = age;
            this.weight = weight;
            addNonNullFeature(Features.doubleFeature(height, "height"));
            addNonNullFeature(Features.integerFeature(age, "age"));
            addNonNullFeature(Features.integerFeature(weight, WEIGHT));
        }
    }
}
