package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.Score;
import com.acepero13.research.profilesimilarity.core.classifier.result.Result;
import com.acepero13.research.profilesimilarity.core.proxy.VectorizableProxy;
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

/**
 * A utility class for performing K-Nearest Neighbor classification on a dataset with mixed feature types.
 * The dataset can consist of both numerical and categorical features.
 */
public class KnnMixedData {
    private final List<FeatureVector> dataSet;
    private final List<Vector<Double>> numericalDataSet;
    private final int k;
    private final List<List<CategoricalFeature<?>>> categoricalDataSet;

    /**
     * Creates a new KnnMixedData object with the given k value and dataset of FeatureVectors.
     *
     * @param k       the number of nearest neighbors to consider
     * @param dataSet the dataset of FeatureVectors to use for classification
     */
    private KnnMixedData(int k, List<FeatureVector> dataSet) {
        this.dataSet = dataSet;
        this.k = k;
        this.numericalDataSet = dataSet.stream()
                .map(FeatureVector::toDouble).collect(Collectors.toList());
        this.categoricalDataSet = dataSet.stream()
                .map(FeatureVector::categorical)
                .collect(Collectors.toList());

    }

    /**
     * Creates a new KnnMixedData object with the given k value and list of Vectorizable objects.
     * Converts each Vectorizable object to a FeatureVector before creating the dataset.
     *
     * @param k       the number of nearest neighbors to consider
     * @param dataSet the list of Vectorizable objects to use for classification
     * @return a new KnnMixedData object with the given k value and dataset of FeatureVectors
     */
    public static KnnMixedData ofVectorizable(int k, List<Vectorizable> dataSet) {
        var featureVectors = dataSet.stream().map(Vectorizable::toFeatureVector)
                .collect(Collectors.toList());
        return new KnnMixedData(k, featureVectors);
    }

    /**
     * Creates a new KnnMixedData object with the given k value and array of Vectorizable objects.
     * Converts each Vectorizable object to a FeatureVector before creating the dataset.
     *
     * @param k       the number of nearest neighbors to consider
     * @param dataSet the array of Vectorizable objects to use for classification
     * @return a new KnnMixedData object with the given k value and dataset of FeatureVectors
     */
    public static KnnMixedData ofVectorizable(int k, Vectorizable... dataSet) {
        return ofVectorizable(k, List.of(dataSet));
    }

    /**
     * Creates a new KnnMixedData object with the given k value and array of FeatureVector objects.
     *
     * @param k       the number of nearest neighbors to consider
     * @param dataSet the array of FeatureVector objects to use for classification
     * @return a new KnnMixedData object with the given k value and dataset of FeatureVectors
     */
    public static KnnMixedData of(int k, FeatureVector... dataSet) {
        return new KnnMixedData(k, List.of(dataSet));
    }


    /**
     * Creates a new KnnMixedData object with the given k value and list of FeatureVector objects.
     *
     * @param k       the number of nearest neighbors to consider
     * @param dataSet the list of FeatureVector objects to use for classification
     * @return a new KnnMixedData object with the given k value and dataset of FeatureVectors
     */
    public static KnnMixedData of(int k, List<FeatureVector> dataSet) {
        return new KnnMixedData(k, dataSet);
    }


    /**
     * Returns a new KnnMixedData object constructed from a list of Vectorizable objects.
     * Each Vectorizable object is converted to a FeatureVector, and the resulting list of FeatureVectors
     * is used to construct a new KnnMixedData object.
     *
     * @param k       the number of nearest neighbors to consider for classification/regression
     * @param dataSet the list of Vectorizable objects to use for constructing the KnnMixedData object
     * @param <T>     the type of the Vectorizable objects
     * @return a new KnnMixedData object constructed from the list of Vectorizable objects
     */
    public static <T> KnnMixedData ofObjects(int k, List<T> dataSet) {
        return of(k, VectorizableProxy.ofFeatureVector(dataSet));
    }


    /**
     * Returns a Result object representing the k-Nearest Neighbors of the given FeatureVector target.
     *
     * @param target the FeatureVector to find the k-Nearest Neighbors of
     * @return a Result object representing the k-Nearest Neighbors of the target FeatureVector
     */
    public Result fit(FeatureVector target) {
        var metric = new GowerMetric();

        List<Tuple<Double, FeatureVector>> scores = metric.calculate(target);
        List<Score> similarNeighbors = scores.stream()
                .parallel()
                .sorted(Comparator.comparingDouble(Tuple::first))
                .limit(k)
                .map(t -> new Score(t.first(), t.second()))
                .collect(Collectors.toList());


        return Result.of(similarNeighbors);
    }

    /**
     * Returns a Result object representing the k-Nearest Neighbors of the given Vectorizable target.
     *
     * @param target the Vectorizable object to find the k-Nearest Neighbors of
     * @return a Result object representing the k-Nearest Neighbors of the target Vectorizable object
     */
    public Result fit(Vectorizable target) {
        return fit(target.toFeatureVector());
    }

    /**
     * Returns a Result object representing the k-Nearest Neighbors of the given Vectorizable target.
     *
     * @param target the Vectorizable object (as a Proxy) to find the k-Nearest Neighbors of
     * @return a Result object representing the k-Nearest Neighbors of the target Vectorizable object
     */
    public Result fit(Object target) {
        return fit(VectorizableProxy.of(target));
    }

    private class GowerMetric {


        private final Matrix<Double> matrix;

        public GowerMetric() {
            this.matrix = Matrix.of(numericalDataSet);
        }

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
            return Matrix.of(categoricalDataSet.stream()
                    .map(l -> categoricalMatchBetween(l, categorical))
                    .collect(Collectors.toList()));
        }

        private Vector<Double> categoricalMatchBetween(List<CategoricalFeature<?>> categorical, List<CategoricalFeature<?>> target) {
            List<CategoricalFeature<?>> filteredCategorical = categorical.stream()
                    .filter(c -> target.stream()
                            .anyMatch(t -> t
                                    .featureName()
                                    .equals(c.featureName())))
                    .collect(Collectors.toList());

            return ListUtils.zip(target, filteredCategorical, CategoricalFeature::matches)
                    .map(v -> v
                            ? 0.0
                            : 1.0)
                    .collect(VectorCollector.toVector());
        }

        private Matrix<Double> calculateNumericalScore(Vector<Double> difference, Vector<Double> numericalTarget) {
            return matrix.map(v -> numericalTarget.subtract(v).abs().divide(difference));

        }
    }
}
