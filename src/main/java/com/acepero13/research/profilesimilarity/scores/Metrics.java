package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

/**
 * A utility class for creating and obtaining different metrics for measuring distances or similarities between data points.
 */
public final class Metrics {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Metrics() {
    }

    /**
     * Returns a metric for computing cosine similarity between two vectors.
     *
     * @return a metric for cosine similarity.
     */
    public static Metric cosineSimilarity() {
        return new CosineMetric();
    }

    /**
     * Returns a metric for computing Jaccard distance between two sets.
     *
     * @return a metric for Jaccard distance.
     */
    public static Metric jaccardDistance() {
        return new JaccardMetric();
    }

    /**
     * Returns a metric for computing normalized Euclidean distance between two vectors.
     *
     * @return a metric for normalized Euclidean distance.
     */
    public static Metric normalizedEuclideanDistance() {
        return new EuclideanDistance();
    }

    /**
     * Returns a metric for computing Euclidean distance between two vectors.
     *
     * @return a metric for Euclidean distance.
     */
    public static Metric euclideanDistance() {
        return NormalizedVector::distanceTo;
    }

    /**
     * Returns a metric for computing Gowers distance between two vectors, which is a combination of Cosine similarity and
     * Strict Dice similarity between categorical variables.
     *
     * @return a metric for Gowers distance.
     */
    public static Metric gowersMetricCosineAndDice() {
        return new GowersMetric(new CosineMetric(), new StrictDiceSimilarity());
    }

    /**
     * Returns a metric for computing Gowers distance between two vectors, using the specified metrics for numerical and
     * categorical variables respectively.
     *
     * @param numericalMetric   the metric to use for numerical variables.
     * @param categoricalMetric the metric to use for categorical variables.
     * @return a metric for Gowers distance.
     */
    public static Metric gowersMetric(Metric numericalMetric, Metric categoricalMetric) {
        return new GowersMetric(numericalMetric, categoricalMetric);
    }
}