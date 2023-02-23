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

 The {@code Vector} interface represents a vector of elements of type {@code T extends Number}.

 @param <T> the type of the elements in the vector, which extends {@link Number}.
 */
public interface Vector<T extends Number> {
    /**

     Constructs a new {@code Vector} from a list of features.
     @param features a {@link List} of features.
     @param <T> the type of the elements in the vector, which extends {@link Number}.
     @return a new {@code Vector} object of type {@code T}.
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

     Returns the norm (magnitude) of this vector.
     @return the norm of this vector as a {@link Double}.
     */
    Double norm();

    /**
     Computes the cosine similarity between this vector and another vector.
     @param another the other vector.
     @return the cosine similarity between this vector and another vector as a {@link Double}.
     @throws VectorException if the two vectors have different dimensions.
     */
    Double cosine(Vector<T> another) throws VectorException;

    /**

     Computes the dot product of this vector and another vector.
     @param another the other vector.
     @return the dot product of this vector and another vector as a {@link Double}.
     @throws VectorException if the two vectors have different dimensions.
     */
    Double dot(Vector<T> another) throws VectorException;
    /**

     Computes the Euclidean distance between this vector and another vector.
     @param another the other vector.
     @return the Euclidean distance between this vector and another vector as a {@link Double}.
     @throws VectorException if the two vectors have different dimensions.
     */
    Double distanceTo(Vector<T> another) throws VectorException;
    /**

     Adds another vector to this vector.
     @param another the other vector.
     @return a new vector that is the result of adding another vector to this vector.
     @throws VectorException if the two vectors have different dimensions.
     */
    Vector<T> add(Vector<T> another) throws VectorException;
    /**

     Subtracts another vector from this vector.
     @param another the other vector.
     @return a new vector that is the result of subtracting another vector from this vector.
     @throws VectorException if the two vectors have different dimensions.
     */
    Vector<T> subtract(Vector<T> another) throws VectorException;
    /**

     Checks whether the size (dimension) of this vector matches that of another vector.
     @param another the other vector.
     @throws VectorException if the two vectors have different dimensions.
     */
    void checkSizeMatchWith(Vector<T> another) throws VectorException;
    /**

     Returns a {@link Stream} of {@link Tuple} objects, where each tuple contains the corresponding elements
     from this vector and another vector.
     @param another the other vector.
     @return a {@link Stream} of {@link Tuple} objects.
     */
    Stream<Tuple<Double, Double>> zip(Vector<T> another);

    Vector<T> multiply(Vector<T> another) throws VectorException;

    T getFeature(int index);

    int size();

    MinMax minMax();


    Vector<Double> divide(Vector<Double> difference);

    double sum();

    Vector<T> divide(double value);

    Vector<Double> toDouble();

    Vector<T> abs();

    Vector<T> add(Vector<T> anotherVector, T padding);
}
