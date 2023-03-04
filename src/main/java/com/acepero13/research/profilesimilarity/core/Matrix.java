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

/**
 * The Matrix class represents a mathematical matrix, which is a two-dimensional array of numbers.
 * <p>
 * The class implements the Iterable interface to allow iteration over the rows of the matrix.
 * <p>
 * The type parameter T specifies the type of the elements of the matrix, which must extend the Number class.
 *
 * @param <T> the type of the Matrix. It must extend {@link Number}
 */
@EqualsAndHashCode
public class Matrix<T extends Number> implements Iterable<Vector<T>> {
    private final List<Vector<T>> matrix;

    /**
     * Constructs a Matrix object from a list of row vectors.
     *
     * @param rows a list of row vectors
     * @throws NullPointerException if rows is null
     */
    private Matrix(List<Vector<T>> rows) {
        this.matrix = Objects.requireNonNull(rows, "Rows cannot be null");
    }

    /**
     * Returns a new Matrix object from a list of row vectors.
     *
     * @param rows a list of row vectors
     * @param <T>  the type of the elements of the matrix, which must extend the Number class
     * @return a new Matrix object
     */
    public static <T extends Number> Matrix<T> of(List<Vector<T>> rows) {
        return new Matrix<>(rows);
    }

    /**
     * Returns a Normalizer object that is built using the minimum and maximum values of the elements
     * of a Matrix of Doubles.
     *
     * @param matrix a Matrix of Doubles to normalize
     * @return a Normalizer object
     */
    public static Normalizer buildMinMaxNormalizerFrom(Matrix<Double> matrix) {
        return Normalizer.minMaxNormalizer(matrix);
    }

    /**
     * Constructs a Matrix object from a list of column vectors.
     *
     * @param vectors a list of column vectors
     * @return a new Matrix object
     */
    public static Matrix<Double> ofVectors(List<Vector<Double>> vectors) {
        return new Matrix<>(vectors);
    }


    /**
     * Returns a new Matrix object that is the transpose of the original matrix.
     *
     * @return a new Matrix object that is the transpose of the original matrix
     * @throws MatrixException if the number of columns of the original matrix does not match the number of rows
     */
    public Matrix<T> transpose() throws MatrixException {
        checkNumberOfColumnsMatch();
        int totalCols = matrix.get(0).size();
        return new Matrix<>(transpose(totalCols));
    }

    /**
     * Returns a list of column vectors that is the transpose of the original matrix.
     *
     * @param totalCols the total number of columns in the original matrix
     * @return a list of column vectors that is the transpose of the original matrix
     */
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

    /**
     * Reduces the matrix column-wise by applying the specified mapper function to each column vector.
     *
     * @param mapper a function that maps a column vector to an object of type R
     * @param <R>    the type of the result of the mapper function
     * @return a list of objects of type R
     */

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


    /**
     * Row iterator
     *
     * @return An iterator of {@link Vector}
     */
    @Override
    public Iterator<Vector<T>> iterator() {
        return matrix.iterator();
    }

    /**
     * Sums each column
     *
     * @return {@link Vector} with the result of sum of every column
     */
    public Vector<Double> sumColumns() {
        return Vector.of(reduceColumnWise(Vector::sum));
    }

    /**
     * Returns the total number of rows
     *
     * @return number of rows
     */
    public double totalRows() {
        return matrix.size();
    }

    /**
     * Applies the given function to each vector in this matrix and returns a new matrix containing the resulting vectors.
     *
     * @param mapper the function to apply to each vector in this matrix
     * @param <R>    the type of the numbers in the resulting matrix
     * @return a new matrix containing the resulting vectors after applying the given function to each vector in this matrix
     */
    public <R extends Number> Matrix<R> map(Function<Vector<T>, Vector<R>> mapper) {
        return new Matrix<>(matrix.stream()
                .parallel().map(mapper)
                .collect(Collectors.toList()));
    }

    /**
     * Adds another matrix to this matrix and returns the resulting matrix.
     *
     * @param anotherMatrix the matrix to be added to this matrix.
     * @return the resulting matrix after adding the two matrices.
     */
    public Matrix<T> add(Matrix<T> anotherMatrix) {
        return new Matrix<>(ListUtils.zip(matrix, anotherMatrix.matrix, Vector::add).collect(Collectors.toList()));
    }

    /**
     * Adds another matrix to this matrix element-wise, with padding for vectors of different sizes, and returns the resulting matrix.
     *
     * @param another the matrix to be added to this matrix.
     * @param padding the padding value to use for vectors of different sizes.
     * @return the resulting matrix after adding the two matrices element-wise.
     */
    public Matrix<T> add(Matrix<T> another, T padding) {
        return new Matrix<>(ListUtils.zip(matrix, another.matrix, (v1, v2) -> v1.add(v2, padding)).collect(Collectors.toList()));
    }

    /**
     * Returns the total number of columns in the matrix.
     * If the matrix has no rows, it returns 0.
     *
     * @return The number of columns in the matrix.
     */
    public int totalColumns() {
        if (totalRows() == 0) {
            return 0;
        }
        return matrix.get(0).size();
    }

    /**
     * Returns a sequential {@link Stream} of the vectors in this matrix.
     *
     * @return A sequential stream of the vectors in this matrix.
     */

    public Stream<Vector<T>> stream() {
        return matrix.stream();
    }
}
