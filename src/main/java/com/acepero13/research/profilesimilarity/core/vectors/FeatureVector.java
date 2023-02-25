package com.acepero13.research.profilesimilarity.core.vectors;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.features.AbstractNumericalFeature;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.NumericalFeature;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link Vector} implementation that represents a collection of numerical features with double values.
 */
@Data
public class FeatureVector implements Vector<AbstractNumericalFeature<Double>> {

    private final List<CategoricalFeature<?>> categorical;
    private final Vector<Double> vector;
    private final List<Feature<?>> features;

    private FeatureVector(List<Feature<?>> features) {

        this.categorical = features.stream()
                .filter(CategoricalFeature.class::isInstance)
                .map(f -> (CategoricalFeature<?>) f)
                .collect(Collectors.toList());
        this.vector = features.stream()
                .filter(f -> f instanceof AbstractNumericalFeature)
                .map(f -> ((AbstractNumericalFeature<?>) f).doubleValue())
                .collect(VectorCollector.toVector());
        this.features = features;


    }

    private FeatureVector(Vector<Double> vector, List<CategoricalFeature<?>> categorical, List<Feature<?>> features) {
        this.vector = vector;
        this.categorical = categorical;
        this.features = features;

    }

    /**
     * Creates a new FeatureVector object from a list of {@link Feature}s, where each feature provides its own numerical value.
     *
     * @param features the list of features to create the FeatureVector object from.
     * @return a new FeatureVector object initialized with the specified values, where each feature's numerical value is collected into a list of {@link AbstractNumericalFeature<Double>}.
     */
    public static FeatureVector of(List<Feature<?>> features) {
        return new FeatureVector(features);
    }

    @Override
    public Double norm() {
        return vector.norm();
    }

    @Override
    public Double cosine(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return vector.cosine(another.toDouble());
    }

    @Override
    public Double dot(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return vector.dot(another.toDouble());
    }

    @Override
    public Double distanceTo(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return vector.distanceTo(another.toDouble());
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> add(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return new FeatureVector(vector.add(another.toDouble()), categorical, features);
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> subtract(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return new FeatureVector(vector.subtract(another.toDouble()), categorical, features);
    }

    @Override
    public void checkSizeMatchWith(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        vector.checkSizeMatchWith(another.toDouble());
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<AbstractNumericalFeature<Double>> another) {
        return vector.zip(another.toDouble());
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> multiply(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return new FeatureVector(vector.multiply(another.toDouble()), categorical, features);
    }

    @Override
    public AbstractNumericalFeature<Double> getFeature(int index) {
        throw new UnsupportedOperationException("t.b.d");
    }

    @Override
    public int size() {
        return this.categorical.size() + this.vector.size();
    }

    @Override
    public MinMax minMax() {
        return vector.minMax();
    }

    @Override
    public Vector<Double> divide(Vector<Double> difference) {
        return vector.divide(difference);
    }

    @Override
    public double sum() {
        return vector.sum();
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> divide(double value) {
        return new FeatureVector(vector.divide(value), categorical, features);
    }

    @Override
    public Vector<Double> toDouble() {
        return vector;
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> abs() {
        return new FeatureVector(vector.abs(), categorical, features);
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> add(Vector<AbstractNumericalFeature<Double>> anotherVector, AbstractNumericalFeature<Double> padding) {
        return new FeatureVector(vector.add(anotherVector.toDouble(), padding.doubleValue()), categorical, features);
    }


    /**
     * Returns a list of {@link CategoricalFeature}  features
     * @return list of categorical features
     */
    public List<CategoricalFeature<?>> categorical() {
        return categorical;
    }

    /**
     * Returns a {@link CategoricalFeature} given its name
     * @param featureName the name of the feature we want to obtain
     * @return {@link Optional} of the feature
     */

    public Optional<CategoricalFeature<?>> getCategoricalFeatureBy(String featureName) {
        return categorical.stream().filter(c -> c.featureName().equals(featureName)).findFirst();
    }

    /**
     * Returns a {@link NumericalFeature} given its name
     * @param featureName the name of the feature we want to obtain
     * @return {@link Optional} of the feature
     */

    public Optional<Feature<?>> getNumericalFeatureBy(String featureName) {
        return features.stream()
                .filter(f -> f.featureName().equals(featureName))
                .filter(f -> f instanceof NumericalFeature)
                .findFirst();
    }

    /**
     * Returns a {@link CategoricalFeature} given the type of its {@link CategoricalFeature#originalValue()}
     * @param type the type of the feature we want to obtain
     * @return {@link Optional} of the feature
     */
    public Optional<CategoricalFeature<?>> getCategoricalFeatureBy(Class<? extends CategoricalFeature<?>> type) {
        return categorical.stream().filter(type::isInstance).findFirst();
    }
}
