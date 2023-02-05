package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.scores.VectorWrapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DataSetTest {
    @Test
    @Disabled
    void normalize() {
        var vector1 = new VectorWrapper(DoubleVector.of(10, 20, 1));
        var vector2 = new VectorWrapper(DoubleVector.of(100, 200, 0));
        var vector3 = new VectorWrapper(DoubleVector.of(1000, 2000, 1));
        final DataSet dataSet = new DataSet(vector1, vector2, vector3);

       // dataSet.normalizer();

        assertThat(vector1.vector(), equalTo(DoubleVector.of(0, 0, 1)));
        assertThat(vector3.vector(), equalTo(DoubleVector.of(1, 1, 1)));
    }



}