package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.exceptions.MatrixException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
public class Matrix<T extends Number> implements Iterable<Vector<T>> {
    private final List<Vector<T>> matrix;

    public Matrix(List<Vector<T>> rows) {
        this.matrix = Objects.requireNonNull(rows, "Rows cannot be null");
    }

    public static List<Vector<Double>> normalizeUsingMinMax(Matrix<Double> matrix) {
        Normalizer normalizer = buildMinMaxNormalizerFrom(matrix);
        List<Vector<Double>> rows = new ArrayList<>();
        for (Vector<Double> row : matrix) {
            rows.add(normalizer.normalize(row));
        }

        return rows;
    }

    public static Normalizer buildMinMaxNormalizerFrom(Matrix<Double> matrix) {
        return Normalizer.minMaxNormalizer(matrix);
    }

    public static Matrix<Double> of(Vectorizable[] vectorizables) {
        List<Vector<Double>> vectors = Stream.of(vectorizables)
                .map(Vectorizable::vector)
                .collect(Collectors.toList());
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


    public static UnaryOperator<Double> minMaxNormalization(MinMax minMax) {
        return (Double val) -> (val - minMax.min()) / minMax.difference();
    }

    @Override
    public Iterator<Vector<T>> iterator() {
        return matrix.iterator();
    }
}
