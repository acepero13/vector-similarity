package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.exceptions.MatrixException;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
public class Matrix<T extends Number> implements Iterable<Vector<T>> {
    private final List<Vector<T>> matrix;

    private Matrix(List<Vector<T>> rows) {
        this.matrix = Objects.requireNonNull(rows, "Rows cannot be null");
    }

    public static <T extends Number> Matrix<T> of(List<Vector<T>> rows) {
        return new Matrix<>(rows);
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
        return transposed.matrix.stream()
                .parallel()
                .map(mapper)
                .collect(Collectors.toList());
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

    public Vector<Double> sumColumns() {
        return Vector.of(reduceColumnWise(Vector::sum));
    }

    public double totalRows() {
        return matrix.size();
    }

    public <R extends Number> Matrix<R> map(Function<Vector<T>, Vector<R>> mapper) {
        return new Matrix<>(matrix.stream()
                .parallel().map(mapper)
                .collect(Collectors.toList()));
    }

    public Matrix<T> add(Matrix<T> anotherMatrix) {
        return new Matrix<>(ListUtils.zip(matrix, anotherMatrix.matrix, Vector::add).collect(Collectors.toList()));
    }

    public Matrix<T> add(Matrix<T> another, T padding) {
        return new Matrix<>(ListUtils.zip(matrix, another.matrix, (v1, v2) -> v1.add(v2, padding)).collect(Collectors.toList()));
    }

    public int totalColumns() {
        if (totalRows() == 0) {
            return 0;
        }
        return matrix.get(0).size();
    }

    public Stream<Vector<T>> stream() {
        return matrix.stream();
    }
}
