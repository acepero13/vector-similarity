package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import lombok.Data;

import java.util.List;


/**
 * Represents a mixed sample with both numerical and categorical features.
 */
@Data
public class MixedSample {

    /**
     * The numerical features of the mixed sample represented as a NormalizedVector.
     */
    private final NormalizedVector vector;
    /**
     * The categorical features of the mixed sample.
     */
    private final List<CategoricalFeature<?>> features;

    /**
     * Creates a new MixedSample object with the given numerical and categorical features.
     *
     * @param vector   the numerical features of the mixed sample represented as a NormalizedVector.
     * @param features the categorical features of the mixed sample.
     */
    private MixedSample(NormalizedVector vector, List<CategoricalFeature<?>> features) {
        this.vector = vector;
        this.features = features;
    }

    /**
     * Creates a new MixedSample object with the given numerical and categorical features.
     * This is a convenience method that uses the of() factory method instead of the constructor.
     *
     * @param sample   the numerical features of the mixed sample represented as a NormalizedVector.
     * @param features the categorical features of the mixed sample.
     * @return a new MixedSample object with the given numerical and categorical features.
     */
    public static MixedSample of(NormalizedVector sample, List<CategoricalFeature<?>> features) {
        return new MixedSample(sample, features);
    }
}