package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.exceptions.VectorException;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class NormalizedVector implements Vector<Double> {
    private final Vector<Double> vector;

    public NormalizedVector(Vector<Double> original, Normalizer normalizer) {
        this.vector = normalizer.normalize(original);
    }

    public static NormalizedVector of(Vector<Double> vector, Normalizer normalizer) {
        return new NormalizedVector(vector, normalizer);
    }

    public static NormalizedVector of(DoubleVector vector) {
        List<UnaryOperator<Double>> mapper = new ArrayList<>();
        for (int i = 0; i < vector.size(); i++) {
            mapper.add(v -> v);
        }
        return new NormalizedVector(vector, v -> v.mapEach(mapper));
    }

    @Override
    public Double norm() {
        return vector.norm();
    }

    @Override
    public Double cosine(Vector<Double> another) throws VectorException {
        return vector.cosine(another);
    }

    @Override
    public Double dot(Vector<Double> another) throws VectorException {
        return vector.dot(another);
    }

    @Override
    public Double distanceTo(Vector<Double> another) throws VectorException {
        return vector.distanceTo(another);
    }

    @Override
    public Vector<Double> add(Vector<Double> another) throws VectorException {
        return vector.add(another);
    }

    @Override
    public Vector<Double> subtract(Vector<Double> another) throws VectorException {
        return vector.subtract(another);
    }

    @Override
    public void checkSizeMatchWith(Vector<Double> another) {
        vector.checkSizeMatchWith(another);
    }

    @Override
    public Stream<Tuple<Double, Double>> zip(Vector<Double> another) {
        return vector.zip(another);
    }

    @Override
    public Vector<Double> multiply(Vector<Double> another) throws VectorException {
        return vector.multiply(another);
    }

    @Override
    public double getFeature(int index) {
        return vector.getFeature(index);
    }

    @Override
    public int size() {
        return vector.size();
    }

    @Override
    public MinMax minMax() {
        return vector.minMax();
    }

    @Override
    public DoubleVector mapEach(List<UnaryOperator<Double>> mapper) {
        return vector.mapEach(mapper);
    }
}
