package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KnnRegression {
    private final DataSet dataSet;
    private final int k;

    public KnnRegression(int k, List<Vectorizable> samples) {
        this.k = k;
        this.dataSet = new DataSet(Vector::distanceTo, samples);

    }


    public Vector<Double> classify(Vectorizable target) {
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<Vector<Double>> results = dataSet.loadDataUsing(target, normalizer)
                .sorted(Comparator.comparingDouble(Tuple::second))
                .limit(k)
                .map(Tuple::first)
                .map(Vectorizable::vector)
                .collect(Collectors.toList());

        return average(results);


    }

    private Vector<Double> average(List<Vector<Double>> results) {
        Matrix<Double> matrix = Matrix.ofVectors(results);
        return Vector.of(matrix.reduceColumnWise(Vector::sum)).divide(results.size());
    }
}
