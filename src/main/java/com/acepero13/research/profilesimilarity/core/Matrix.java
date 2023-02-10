package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.exceptions.MatrixException;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class Matrix<T extends Number> implements Iterable<Vector<T>> {
    private final List<Vector<T>> matrix;

    public Matrix(List<Vector<T>> rows) {
        this.matrix = Objects.requireNonNull(rows, "Rows cannot be null");
    }


    public static Normalizer buildMinMaxNormalizerFrom(Matrix<Double> matrix) {
        return Normalizer.minMaxNormalizer(matrix);
    }


    public static Matrix<Double> ofVectors(List<Vector<Double>> vectors) {
        return new Matrix<>(vectors);
    }


    public Matrix<T> transpose() throws MatrixException {
        checkNumberOfColumnsMatch();
        int totalCols = matrix.get(0).size();
        return new Matrix<>(transpose(totalCols));
    }

    private List<Vector<T>> transpose(int totalCols) {
        List<Vector<T>> transposed = new ArrayList<>();
        for (int column = 0; column < totalCols; column++) {
            List<T> tmp = new ArrayList<>();
            for (Vector<T> vector : matrix) {
                tmp.add(vector.getFeature(column));
            }
            transposed.add(Vector.of(tmp));
        }
        return transposed;
    }


    public <R> List<R> reduceColumnWise(Function<Vector<T>, R> mapper) {
        Matrix<T> transposed = transpose();
        return transposed.matrix.stream().map(mapper).collect(Collectors.toList());
    }

    private void checkNumberOfColumnsMatch() throws MatrixException {
        int size = -1;
        int row = 1;
        for (Vector<T> vector : matrix) {
            if (size == -1) {
                size = vector.size();
            }
            if (vector.size() != size) {
                throw new MatrixException("Not every vector have the same size. The expected size is: " + size + " but row: " + row + " has size: " + vector.size());
            }
            row++;
        }
    }



    @Override
    public Iterator<Vector<T>> iterator() {
        return matrix.iterator();
    }

    public Vector<T> sumColumns() {
        return Vector.of(reduceColumnWise(Vector::sum));
    }
}
