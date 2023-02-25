package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Matrix;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.proxy.VectorizableProxy;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The Knn (K-nearest neighbors) algorithm implementation that finds the k-nearest neighbors of a given data point
 * <p>
 * and determines its classification based on the classification of its neighbors.
 */
@Log
public class Knn {

    private final int k;
    private final DataSet dataSet;
    private Normalizer normalizer;
    private List<Tuple<Vectorizable, NormalizedVector>> normalizedDataSet = new ArrayList<>();


    private Knn(int k, List<Vectorizable> data) {
        this.k = k;
        this.dataSet = new DataSet(requireNonNull(data));
    }

    private Knn(int k, Normalizer normalizer, List<Vectorizable> data) {
        this.k = k;
        this.dataSet = new DataSet(requireNonNull(data));
        this.normalizer = normalizer;
    }

    /**
     * Constructs a new instance of the Knn algorithm with the specified k value and normalizer.
     *
     * @param k          the number of neighbors to consider
     * @param normalizer the normalizer to use
     * @param data       the data set
     * @return a new instance of the Knn algorithm
     */
    public static Knn of(int k, Normalizer normalizer, List<Vectorizable> data) {
        return new Knn(k, normalizer, data);
    }

    /**
     * Constructs a new instance of the Knn algorithm with the specified k value and the default normalizer. The default
     * normalizer is the {@link Normalizer#minMaxNormalizer(Matrix)}
     *
     * @param k    the number of neighbors to consider
     * @param data the data set
     * @return a new instance of the Knn algorithm
     */
    public static Knn withDefaultNormalizer(int k, List<Vectorizable> data) {
        return new Knn(k, data);
    }

    /**
     * Constructs a new instance of the Knn algorithm with the specified k value and the default normalizer. The default
     * normalizer is the {@link Normalizer#minMaxNormalizer(Matrix)}
     *
     * @param k    the number of neighbors to consider
     * @param data the data set
     * @return a new instance of the Knn algorithm
     */
    public static Knn withDefaultNormalizer(int k, Vectorizable... data) {
        return new Knn(k, List.of(data));
    }


    /**
     * Constructs a new instance of the Knn algorithm with the specified k value and the default normalizer. The default
     * normalizer is the {@link Normalizer#minMaxNormalizer(Matrix)}
     *
     * @param <T>  the type of objects in the data set
     * @param k    the number of neighbors to consider
     * @param data the data set
     * @return a new instance of the Knn algorithm
     */
    public static <T> Knn ofObjectsWithDefaultNormalizer(int k, List<T> data) {
        return new Knn(k, VectorizableProxy.of(data));
    }

    /**
     * Constructs a new instance of the Knn algorithm with the specified k value and normalizer.
     *
     * @param <T>        the type of objects in the data set
     * @param k          the number of neighbors to consider
     * @param normalizer the normalizer to use
     * @param data       the data set
     * @return a new instance of the Knn algorithm
     */
    public static <T> Knn ofObjects(int k, Normalizer normalizer, List<T> data) {
        return new Knn(k, normalizer, VectorizableProxy.of(data));
    }


    private static Comparator<DataSet.Score> ascendingScore() {
        return Comparator.comparingDouble(DataSet.Score::score);
    }

    /**
     * Fits the Knn algorithm to the given target vectorizable and returns a KnnResult object containing the predicted
     * label and the distances to the k nearest neighbors.
     *
     * @param target the target vectorizable to fit the algorithm to
     * @return a KnnResult object containing the predicted label and the distances to the k nearest neighbors
     * @throws NullPointerException if the target vectorizable is null
     */
    public KnnResult fit(Vectorizable target) {
        requireNonNull(target, "Target cannot be null");
        logInitialInformation();
        NormalizedVector normalizedTarget = normalize(target);
        return classify(normalizedTarget);
    }

    /**
     * Fits the Knn algorithm to the given target object by creating a VectorizableProxy and returns a KnnResult object
     * containing the predicted label and the distances to the k nearest neighbors.
     *
     * @param target the target object to fit the algorithm to
     * @return a KnnResult object containing the predicted label and the distances to the k nearest neighbors
     * @throws NullPointerException if the target object is null
     */

    public KnnResult fit(Object target) {
        return fit(VectorizableProxy.of(target));
    }

    private void logInitialInformation() {
        log.info(String.format("Classifying using Categorical KNN with k=%d.", k));
        log.info("Number of samples: " + dataSet.size());
        if (CalculationUtils.isEvenNumber(k)) {
            log.warning("K: {} is an even number. Consider changing it to an odd number to help the voting process");
        }
    }

    private KnnResult classify(NormalizedVector normalizedTarget) {


        List<FeatureVector> results = normalizedDataSet.stream()
                .parallel()
                .map(t -> t.mapSecond(v -> DataSet.calculateScore(Vector::distanceTo, normalizedTarget, v)))
                .map(t -> new DataSet.Score(t.second(), t.first()))
                .sorted(ascendingScore())
                .limit(k)
                .map(DataSet.Score::sample)
                .map(Vectorizable::toFeatureVector)
                .collect(Collectors.toList());

        return KnnResult.of(results);
    }

    private NormalizedVector normalize(Vectorizable target) {
        if (normalizer == null) {
            this.normalizer = DataSet.minMaxNormalizer(target, dataSet);
        }
        if (normalizedDataSet.isEmpty()) {
            normalizedDataSet = dataSet.scaleAndScore(target, normalizer);
        }
        return NormalizedVector.of(target.vector(target.numericalFeatures()), normalizer);
    }

}
