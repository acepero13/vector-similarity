package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VectorTest {

    private final DoubleVector vector1 = DoubleVector.of(1.0, 2.0, 3.0, 4.0, 5.0);
    private final DoubleVector vector2 = DoubleVector.of(List.of(10.0, 20.0, 30.0, 40.0, 50.0));

    @Test
    void multiply() {
        Vector<Double> result = vector1.multiply(vector2);
        Vector<Double> expected = DoubleVector.of(10.0, 40.0, 90.0, 160.0, 250.0);
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
        assertThrows(VectorException.class, () -> vector1.multiply(DoubleVector.of(1.0)));
    }

    @Test
    void sumTwoVectors() {
        Vector<Double> result = vector1.add(vector2);
        Vector<Double> expected = DoubleVector.of(11.0, 22.0, 33.0, 44.0, 55.0);

        assertThat(result, equalTo(expected));
    }

    @Test
    void subtract() {
        Vector<Double> result = vector2.subtract(vector1);
        Vector<Double> expected = DoubleVector.of(9.0, 18.0, 27.0, 36.0, 45.0);
        assertThat(result, equalTo(expected));
    }

    @Test
    void sumTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.add(DoubleVector.of(1.0)));
    }

    @Test
    void subtractTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.subtract(DoubleVector.of(1.0)));
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
        VectorException thrown = assertThrows(VectorException.class, () -> vector1.cosine(DoubleVector.of(1.0, 2.0)));

        assertThat(thrown.getMessage(), containsString("Vector length do not match."));
    }

    @Test
    void cosineSimilarityNormIsZero() {
        assertThat(vector1.cosine(DoubleVector.of(0.0)), closeTo(0.0, 0.1));
    }

    @Test
    void norm() {
        Double norm = DoubleVector.of(1.0, 2.0, 3.0).norm();
        assertThat(norm, closeTo(3.742, 0.1));
    }


    @Test
    void dotProduct() {
        var v1 = DoubleVector.of(1.0, 2.0, 3.0);
        var v2 = DoubleVector.of(4.0, -5.0, 6.0);
        Double product = v1.dot(v2);
        assertThat(product, closeTo(12, 0.1));
    }


    @Test
    void dotTwoDifferentSizedVectors() {
        assertThrows(VectorException.class, () -> vector1.dot(DoubleVector.of(1.0)));
    }

    @Test
    void getFeature() {
        double result = vector1.getFeature(0);
        assertThat(result, closeTo(1.0, 0.01));
    }

    @Test
    void exceptionWhenIndexOutOfBounds() {
         assertThrows(IndexOutOfBoundsException.class, () -> vector1.getFeature(100));

    }

    @Test
    void cannotDivideByZero() {
        assertThrows(VectorException.class, () -> vector1.divide(0));
    }

}
