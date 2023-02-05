package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.VectorWrapper;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {
    @Test
    void normalize() {
        var vector1 = new VectorWrapper(Vector.of(10, 20, 1));
        var vector2 = new VectorWrapper(Vector.of(100, 200, 0));
        var vector3 = new VectorWrapper(Vector.of(1000, 2000, 1));
        final DataSet dataSet = new DataSet(vector1, vector2, vector3);

        var result = dataSet.normalize().stream().map(Vectorizable::vector).collect(Collectors.toList());

        assertThat(vector1.vector(), equalTo(Vector.of(0, 0, 1)));
        assertThat(vector3.vector(), equalTo(Vector.of(1, 1, 1)));
    }

}