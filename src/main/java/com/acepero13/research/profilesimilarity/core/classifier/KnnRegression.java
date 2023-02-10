package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Matrix;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Log
public class KnnRegression {
    private final DataSet dataSet;
    private final int k;

    public KnnRegression(int k, List<Vectorizable> samples) {
        this.k = k;
        this.dataSet = new DataSet(Vector::distanceTo, requireNonNull(samples));

    }

    private static Comparator<DataSet.Score> ascendingScore() {
        return Comparator.comparingDouble(DataSet.Score::score);
    }

    public Vector<Double> classify(Vectorizable target) {
        requireNonNull(target);
        log.info(String.format("Classifying using Categorical KNN with k=%d.", k));
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<Vector<Double>> results = dataSet.scaleAndScore(target, normalizer)
                .sorted(ascendingScore())
                .limit(k)
                .map(DataSet.Score::sample)
                .map(Vectorizable::vector)
                .collect(Collectors.toList());

        return average(results);


    }

    private Vector<Double> average(List<Vector<Double>> results) {
        Matrix<Double> matrix = Matrix.ofVectors(results);
        return matrix.sumColumns().divide(results.size());
    }
}
