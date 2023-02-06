package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.exceptions.MatrixException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatrixTest {
    public static final Matrix<Double> MISMATCHED_MATRIX = new Matrix<>(List.of(DoubleVector.of(1, 2), DoubleVector.of(2, 5, 8)));
    private final Matrix<Double> matrix = new Matrix<>(List.of(DoubleVector.of(1, 2, 3), DoubleVector.of(4, 5, 6), DoubleVector.of(7, 8, 9)));

    @Test
    void transposeMatrix() {
        var expected = new Matrix<>(List.of(DoubleVector.of(1, 4, 7), DoubleVector.of(2, 5, 8), DoubleVector.of(3, 6, 9)));
        Matrix<Double> result = matrix.transpose();

        assertThat(result, equalTo(expected));
    }

    @Test
    void calculateMinMax() {
        var expected = List.of(new MinMax(1, 7), new MinMax(2, 8), new MinMax(3, 9));
        List<MinMax> result = matrix.reduceColumnWise(Vector::minMax);
        assertThat(result, equalTo(expected));
    }

    @Test
    void normalize() {

        Matrix<Double> newMatrix = new Matrix<>(List.of(
                DoubleVector.of(10, 20, 1),
                DoubleVector.of(100, 200, 0),
                DoubleVector.of(1000, 2000, 1)));

        List<Vector<Double>> result = Matrix.normalizeUsingMinMax(newMatrix);

        assertThat(result.get(0), equalTo(DoubleVector.of(0, 0, 1)));
        assertThat(result.get(2), equalTo(DoubleVector.of(1, 1, 1)));

    }

    @Test
    void cannotTrasposeIfNumbersOfColumnsDoNotMatch() {
        MatrixException exception = assertThrows(MatrixException.class, MISMATCHED_MATRIX::transpose);
        assertThat(exception.getMessage(), containsString("Not every vector have the same size. The expected size is: 2 but row: 2 has size: 3"));
    }
}