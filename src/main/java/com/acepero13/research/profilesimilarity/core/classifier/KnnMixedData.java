package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.utils.ListUtils;
import com.acepero13.research.profilesimilarity.utils.MinMaxVector;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class KnnMixedData {
    private final List<FeatureVector> dataSet;
    private final List<Vector<Double>> numericalDataSet;
    private final int k;
    private final List<List<CategoricalFeature<?>>> categoricalDataSet;

    public KnnMixedData(int k, List<FeatureVector> dataSet) {
        this.dataSet = dataSet;
        this.k = k;
        this.numericalDataSet = dataSet.stream().parallel().map(FeatureVector::toDouble).collect(Collectors.toList());
        this.categoricalDataSet = dataSet.stream().parallel().map(FeatureVector::categorical).collect(Collectors.toList());

    }

    public KnnResult fit(FeatureVector target) {
        var metric = new GowerMetric();

        List<Tuple<Double, FeatureVector>> scores = metric.calculate(target);
        List<FeatureVector> similarNeighbors = scores.stream()
                .parallel()
                .sorted(Comparator.comparingDouble(Tuple::first))
                .limit(k)
                .map(Tuple::second)
                .collect(Collectors.toList());


        return KnnResult.of(similarNeighbors);


    }

    private class GowerMetric {


        private final Matrix<Double> matrix;

        public GowerMetric() {
            this.matrix = new Matrix<>(numericalDataSet);
        }

        // TODO: Refactor this
        public List<Tuple<Double, FeatureVector>> calculate(FeatureVector target) {
            MinMaxVector minMaxVector = MinMaxVector.of(matrix);
            Vector<Double> difference = minMaxVector.difference();
            var numericalTarget = target.toDouble();
            Matrix<Double> numericalScore = calculateNumericalScore(difference, numericalTarget);
            Matrix<Double> categoricalScore = calculateCategoricalScore(target.categorical());

            List<Double> finalScore = calculateFinalScore(numericalScore, categoricalScore);


            return buildResult(finalScore);


        }

        private List<Tuple<Double, FeatureVector>> buildResult(List<Double> finalScore) {
            Iterator<FeatureVector> it = dataSet.iterator();
            List<Tuple<Double, FeatureVector>> result = new ArrayList<>();
            int i = 0;
            while (it.hasNext()) {
                var score = finalScore.get(i);
                result.add(Tuple.of(score, it.next()));
                i++;
            }
            return result;
        }

        private List<Double> calculateFinalScore(Matrix<Double> numericalScore, Matrix<Double> categoricalScore) {
            return numericalScore.add(categoricalScore, 0.0)
                    .stream()
                    .parallel()
                    .map(score -> score.sum() / (numericalScore.totalColumns() + categoricalScore.totalColumns()))
                    .collect(Collectors.toList());


        }

        private Matrix<Double> calculateCategoricalScore(List<CategoricalFeature<?>> categorical) {
            return new Matrix<>(categoricalDataSet.stream()
                    .parallel()
                    .map(l -> categoricalMatchBetween(l, categorical))
                    .collect(Collectors.toList()));
        }

        private Vector<Double> categoricalMatchBetween(List<CategoricalFeature<?>> categorical, List<CategoricalFeature<?>> target) {
            List<CategoricalFeature<?>> filteredCategorical = categorical.stream()
                                                                         .filter(c -> target.stream()
                                                                                            .anyMatch(t -> t.featureName().equals(c.featureName())))
                                                                         .collect(Collectors.toList());

            return ListUtils.zip(target, filteredCategorical, CategoricalFeature::matches)
                    .map(v -> v ? 0.0 : 1.0)
                    .collect(VectorCollector.toVector());
        }

        private Matrix<Double> calculateNumericalScore(Vector<Double> difference, Vector<Double> numericalTarget) {
            return matrix.map(v -> numericalTarget.subtract(v).abs().divide(difference));

        }
    }
}
