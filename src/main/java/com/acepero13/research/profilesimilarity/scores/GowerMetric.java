package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;
import java.util.stream.Collectors;

public class GowerMetric {
    public Double similarityScore(List<FeatureVector> dataPoints, FeatureVector vector, FeatureVector another) {
        List<Vector<Double>> dataSet = dataPoints.stream().map(FeatureVector::toDouble).collect(Collectors.toList());
        Vector<Double> anotherNumerical = another.toDouble();
        var numerical = vector.toDouble();

        var matrix = Matrix.ofVectors(dataSet);
        Vector<Double> avg = matrix.sumColumns().divide(matrix.totalRows());
        var scoreNumerical = numerical.subtract(anotherNumerical).divide(avg);
        int a = 0;
        return 0.0;
    }
}
