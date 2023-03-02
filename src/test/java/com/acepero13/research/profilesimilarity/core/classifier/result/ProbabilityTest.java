package com.acepero13.research.profilesimilarity.core.classifier.result;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProbabilityTest {

    @Test
    void valueLessThanZero() {
        var probability = Probability.of(0.5);
        assertThat(probability.asPercentage(), equalTo(50.0));
    }


    @Test
    void valueBiggerThanZero() {
        var probability = Probability.of(50);
        assertThat(probability.asPercentage(), equalTo(50.0));
    }

    @Test
    void valueBiggerThan100() {
        var error = assertThrows(IllegalArgumentException.class, () -> Probability.of(150));
        assertThat(error.getMessage(), equalTo("Maximum value of the probability is 100, but 150.0 was given"));
    }

    @Test
    void testToString() {
        var probability = Probability.of(30);
        assertThat(probability.toString(), equalTo("Probability: 30.0%"));
    }
}