package com.acepero13.research.profilesimilarity.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class MinMaxTest {

    @Test
    void minMaxValues() {
        var m = new MinMax(0, 100);
        assertThat(m.min(), closeTo(0.0, 0.1));
        assertThat(m.max(), closeTo(100.0, 0.1));
    }

    @Test
    void assertDifference() {
        var m = new MinMax(50, 100);
        assertThat(m.difference(), closeTo(50.0, 0.1));
    }
}