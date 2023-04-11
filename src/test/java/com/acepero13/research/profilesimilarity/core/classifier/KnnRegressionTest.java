package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class KnnRegressionTest {

    public static final String WEIGHT = "weight";
    public static final List<Vectorizable> SAMPLES = List.of(
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

    @Test
    void knnRegression() {


        User test = new User("11", 5.5, 38, null);

        Knn classifier = Knn.withDefaultNormalizer(3, SAMPLES);

        var result = classifier.fit(test)
                .predict(WEIGHT);

        assertThat(result, closeTo(57.666666666666664, 0.01));

    }

    @Test
    void knnRegressionWithScore() {

        User test = new User("11", 5.5, 38, null);

        Knn classifier = Knn.withDefaultNormalizer(3, SAMPLES);

        var result = classifier.fit(test)
                               .predictWithScore(WEIGHT);

        assertThat(result.prediction(), closeTo(57.666666666666664, 0.01));
        assertThat(result.score(), closeTo(4.085, 0.1));

    }

    @Test
    void knnRegressionUsingAnnotation() {
        List<AnnotatedUser> samples = List.of(
                new AnnotatedUser("1", 5.0, 45, 77),
                new AnnotatedUser("2", 5.11, 26, 47),
                new AnnotatedUser("3", 5.6, 30, 55),
                new AnnotatedUser("4", 5.9, 34, 59),
                new AnnotatedUser("5", 4.8, 40, 72),
                new AnnotatedUser("6", 5.8, 36, 60),
                new AnnotatedUser("7", 5.3, 19, 40),
                new AnnotatedUser("8", 5.8, 28, 60),
                new AnnotatedUser("9", 5.5, 23, 45),
                new AnnotatedUser("10", 5.6, 32, 58)
        );

        AnnotatedUser test = new AnnotatedUser("11", 5.5, 38, null);

        Knn classifier = Knn.ofObjectsWithDefaultNormalizer(3, samples);

        var result = classifier.fit(test)
                .predict();

        assertThat(result, closeTo(57.666666666666664, 0.01));

    }

    @Data
    @com.acepero13.research.profilesimilarity.annotations.Vectorizable
    private static class AnnotatedUser {

        private final String id;
        @Numerical
        private final Double height;
        @Numerical
        private final int age;
        @Numerical(name = WEIGHT, target = true)
        private final Integer weight;
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
