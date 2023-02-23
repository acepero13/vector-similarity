package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.MixedSample;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.List;

/**
 * An interface representing a metric that can calculate the similarity score between two vectors.
 */
public interface Metric {

    /**
     * Returns a normalized vector representing the given categorical features.
     *
     * @param features the categorical features to be converted to a normalized vector.
     * @return a normalized vector representing the given categorical features.
     */
    static NormalizedVector from(List<CategoricalFeature<?>> features) {
        return NormalizedVector.of(features.stream()
                .map(Feature::originalValue)
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .map(v -> v
                        ? 1.0
                        : 0.0)
                .collect(VectorCollector.toVector()));
    }

    /**
     * Calculates the similarity score between two normalized vectors.
     *
     * @param vectorizable the first normalized vector.
     * @param another      the second normalized vector.
     * @return the similarity score between the two normalized vectors.
     */
    Double similarityScore(NormalizedVector vectorizable, NormalizedVector another);

    /**
     * Calculates the similarity score between two mixed samples.
     *
     * @param sample  the first mixed sample.
     * @param another the second mixed sample.
     * @return the similarity score. If not implemented, this method returns 0.0
     */
    default Double similarityScore(MixedSample sample, MixedSample another) {
        return 0.0;
    }
}