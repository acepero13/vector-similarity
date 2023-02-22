package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.AbstractNumericalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeatureVectorTest {

    public static final FeatureVector ONE = FeatureVector.of(Collections.singletonList(Features.integerFeature(1, "one")));
    public static final FeatureVector ZERO = FeatureVector.of(Collections.singletonList(Features.doubleFeature(0.0, "zero")));
    private static final List<Feature<?>> features1 = new ArrayList<>();
    private static final List<Feature<?>> features2 = new ArrayList<>();

    static {
        features1.add(Features.integerFeature(1, "feat1"));
        features1.add(Features.integerFeature(2, "feat2"));
        features1.add(Features.integerFeature(3, "feat3"));
        features1.add(Features.integerFeature(4, "feat4"));
        features1.add(Features.integerFeature(5, "feat5"));


        features2.add(Features.integerFeature(10, "feat21"));
        features2.add(Features.integerFeature(20, "feat22"));
        features2.add(Features.integerFeature(30, "feat23"));
        features2.add(Features.integerFeature(40, "feat24"));
        features2.add(Features.integerFeature(50, "feat25"));

    }

    private final FeatureVector vector1 = FeatureVector.of(features1);
    private final FeatureVector vector2 = FeatureVector.of(features2);

    @Test
    void multiply() {
        var result = vector1.multiply(vector2).toDouble();
        var expected = DoubleVector.of(10.0, 40.0, 90.0, 160.0, 250.0);
        assertThat(result, equalTo(expected));
    }


    @Test
    void distanceTo() {
        Double result = vector1.distanceTo(vector2);

        double expected = 66.74;
        assertThat(result, closeTo(expected, 0.1));
    }

    @Test
    void multipliesDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.multiply(ONE));
    }

    @Test
    void sumTwoVectors() {
        var result = vector1.add(vector2).toDouble();
        var expected = DoubleVector.of(11.0, 22.0, 33.0, 44.0, 55.0);

        assertThat(result, equalTo(expected));
    }

    @Test
    void subtract() {
        var result = vector2.subtract(vector1);
        var expected = DoubleVector.of(9.0, 18.0, 27.0, 36.0, 45.0);
        assertThat(result.toDouble(), equalTo(expected));
    }

    @Test
    void sumTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.add(ONE));
    }

    @Test
    void subtractTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.subtract(ONE));
    }

    @Test
    void cosineSimilarity() {

        DoubleVector v1 = DoubleVector.of(2.0, 1.0, 2.0, 3.0, 2.0, 9.0);
        DoubleVector v2 = DoubleVector.of(3.0, 4.0, 2.0, 4.0, 5.0, 5.0);

        Double result = v1.cosine(v2);

        assertThat(result, closeTo(0.81, 0.01));
    }

    @Test
    void cosineSimilarityTwoDifferentsized() {
        VectorException thrown = assertThrows(VectorException.class, () -> vector1.cosine(ONE));

        assertThat(thrown.getMessage(), containsString("Vector length do not match."));
    }

    @Test
    void cosineSimilarityNormIsZero() {
        assertThat(vector1.cosine(ZERO), closeTo(0.0, 0.1));
    }

    @Test
    void norm() {
        Double norm = vector1.norm();
        assertThat(norm, closeTo(7.41, 0.1));
    }

    @Test
    void checkDifferentVectorSize() {
        assertThrows(VectorException.class, () -> vector1.checkSizeMatchWith(ONE));
    }

    @Test
    void size() {
        assertThat(vector1.size(), equalTo(5));
    }

    @Test
    void sum() {
        assertThat(vector1.sum(), equalTo(15.0));
    }

    @Test
    void addDifferentSized() {
        var actual = vector1.add(ONE, Features.doubleFeature(0.0, "z")).toDouble();
        assertThat(actual, equalTo(DoubleVector.of(2.0, 2.0, 3.0, 4.0, 5.0)));
    }

    @Test
    void minMax() {
        var actual = vector1.minMax();
        assertThat(actual, equalTo(new MinMax(1.0, 5.0)));
    }


    @Test
    void dotProduct() {

        Double product = vector1.dot(vector2);
        assertThat(product, closeTo(550, 0.1));
    }

    @Test
    void abs() {

        var abs = FeatureVector.of(List.of(f(1), f(-2))).abs().toDouble();
        assertThat(abs, equalTo(DoubleVector.of(1.0, 2.0)));
    }

    private AbstractNumericalFeature<Integer> f(int value) {
        return Features.integerFeature(value, "feat");
    }


    @Test
    void dotTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.dot(ONE));
    }


    @Test
    void exceptionWhenIndexOutOfBounds() {
        UnsupportedOperationException error = assertThrows(UnsupportedOperationException.class, () -> vector1.getFeature(100));

        //assertThat(error.getMessage(), containsString("Index 100 out of bounds for length 5"));
    }

    @Test
    void cannotDivideByZero() {
        assertThrows(VectorException.class, () -> vector1.divide(0));
    }

}
