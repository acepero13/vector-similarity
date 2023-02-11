package com.acepero13.research.profilesimilarity.core.vectors;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.features.AbstractNumericalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.features.NumericalFeature;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeatureVector implements Vector<AbstractNumericalFeature<Double>> {

    private final List<Feature<?>> categorical;
    private final List<Double> numerical;
    private final Vector<Double> vector;

    public FeatureVector(List<Feature<?>> features) {
        this.numerical = features.stream().filter(f -> f instanceof AbstractNumericalFeature).map(f -> ((AbstractNumericalFeature<?>) f).doubleValue()).collect(Collectors.toList());
        this.categorical = features.stream().filter(f -> !(f instanceof NumericalFeature)).collect(Collectors.toList());
        this.vector = DoubleVector.of(numerical);

    }

    public FeatureVector(Vector<Double> vector, List<Feature<?>> categorical) {
        this.vector = vector;
        this.categorical = categorical;
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < vector.size(); i++) {
            values.add(vector.getFeature(i));
        }
        this.numerical = values;

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
        return new FeatureVector(vector.add(another.toDouble()), categorical);
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> subtract(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return new FeatureVector(vector.subtract(another.toDouble()), categorical);
    }

    @Override
    public void checkSizeMatchWith(Vector<AbstractNumericalFeature<Double>> another) {
        vector.checkSizeMatchWith(another.toDouble());
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<AbstractNumericalFeature<Double>> another) {
        return vector.zip(another.toDouble());
    }

    @Override
    public Vector<AbstractNumericalFeature<Double>> multiply(Vector<AbstractNumericalFeature<Double>> another) throws VectorException {
        return new FeatureVector(vector.multiply(another.toDouble()), categorical);
    }

    @Override
    public AbstractNumericalFeature<Double> getFeature(int index) {
        throw new UnsupportedOperationException("t.b.d") ;
    }

    @Override
    public int size() {
        return this.categorical.size() + this.numerical.size();
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
        return new FeatureVector(vector.divide(value), categorical);
    }

    @Override
    public Vector<Double> toDouble() {
        return vector;
    }


}
