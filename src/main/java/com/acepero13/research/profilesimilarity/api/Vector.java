package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code Vector} interface represents a vector of elements of type {@code T extends Number}.
 *
 * @param <T> the type of the elements in the vector, which extends {@link Number}.
 */
public interface Vector<T extends Number> {
    /**
     * Constructs a new {@code Vector} from a list of features.
     *
     * @param features a {@link List} of features.
     * @param <T>      the type of the elements in the vector, which extends {@link Number}.
     * @return a new {@code Vector} object of type {@code T}.
     */
    @SuppressWarnings("unchecked")
    static <T extends Number> Vector<T> of(List<T> features) {
        if (features.stream().allMatch(Double.class::isInstance)) {
            return (Vector<T>) DoubleVector.of(features.stream()
                    .map(Double.class::cast).collect(Collectors.toList()));
        }
        return (Vector<T>) DoubleVector.of(new ArrayList<>());
    }

    /**
     * Returns the norm (magnitude) of this vector.
     *
     * @return the norm of this vector as a {@link Double}.
     */
    Double norm();

    /**
     * Computes the cosine similarity between this vector and another vector.
     *
     * @param another the other vector.
     * @return the cosine similarity between this vector and another vector as a {@link Double}.
     * @throws VectorException if the two vectors have different dimensions.
     */
    Double cosine(Vector<T> another) throws VectorException;

    /**
     * Computes the dot product of this vector and another vector.
     *
     * @param another the other vector.
     * @return the dot product of this vector and another vector as a {@link Double}.
     * @throws VectorException if the two vectors have different dimensions.
     */
    Double dot(Vector<T> another) throws VectorException;

    /**
     * Computes the Euclidean distance between this vector and another vector.
     *
     * @param another the other vector.
     * @return the Euclidean distance between this vector and another vector as a {@link Double}.
     * @throws VectorException if the two vectors have different dimensions.
     */
    Double distanceTo(Vector<T> another) throws VectorException;

    /**
     * Adds another vector to this vector.
     *
     * @param another the other vector.
     * @return a new vector that is the result of adding another vector to this vector.
     * @throws VectorException if the two vectors have different dimensions.
     */
    Vector<T> add(Vector<T> another) throws VectorException;

    /**
     * Subtracts another vector from this vector.
     *
     * @param another the other vector.
     * @return a new vector that is the result of subtracting another vector from this vector.
     * @throws VectorException if the two vectors have different dimensions.
     */
    Vector<T> subtract(Vector<T> another) throws VectorException;

    /**
     * Checks whether the size (dimension) of this vector matches that of another vector.
     *
     * @param another the other vector.
     * @throws VectorException if the two vectors have different dimensions.
     */
    void checkSizeMatchWith(Vector<T> another) throws VectorException;

    /**
     * Returns a {@link Stream} of {@link Tuple} objects, where each tuple contains the corresponding elements
     * from this vector and another vector.
     *
     * @param another the other vector.
     * @return a {@link Stream} of {@link Tuple} objects.
     */
    Stream<Tuple<Double, Double>> zip(Vector<T> another);

    /**
     * Multiplies this vector with another vector and returns the resulting vector.
     *
     * @param another the vector to multiply this vector with.
     * @return the resulting vector.
     * @throws VectorException if the vectors are not of the same size.
     */
    Vector<T> multiply(Vector<T> another) throws VectorException;

    /**
     * Returns the feature at the specified index in this vector.
     *
     * @param index the index of the feature to retrieve.
     * @return the feature at the specified index.
     */
    T getFeature(int index);

    /**
     * Returns the size of this vector.
     *
     * @return the size of this vector.
     */
    int size();

    /**
     * Returns the minimum and maximum values in this vector.
     *
     * @return the minimum and maximum values in this vector.
     */
    MinMax minMax();

    /**
     * Divides this vector by another vector and returns the resulting vector.
     *
     * @param difference the vector to divide this vector by.
     * @return the resulting vector.
     */

    Vector<Double> divide(Vector<Double> difference);

    /**
     * Returns the sum of all the elements in this vector.
     *
     * @return the sum of all the elements in this vector.
     */
    double sum();

    /**
     * Divides all elements in this vector by a specified value and returns the resulting vector.
     *
     * @param value the value to divide all elements in this vector by.
     * @return the resulting vector.
     */
    Vector<T> divide(double value);

    /**
     * Converts all elements in this vector to doubles and returns the resulting vector.
     *
     * @return the resulting vector with all elements converted to doubles.
     */
    Vector<Double> toDouble();

    /**
     * Returns the absolute values of all elements in this vector.
     *
     * @return the resulting vector with all elements converted to their absolute values.
     */
    Vector<T> abs();

    /**
     * Adds another vector to this vector and returns the resulting vector.
     * If the two vectors are not of the same size, the missing elements will be padded with the given value.
     *
     * @param anotherVector the vector to add to this vector.
     * @param padding       the value to use as padding if the two vectors are not of the same size.
     * @return the resulting vector.
     */
    Vector<T> add(Vector<T> anotherVector, T padding);
}
