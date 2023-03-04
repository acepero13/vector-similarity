package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import lombok.Data;

import java.util.List;
import java.util.Optional;


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

    /**
     * Returns true if a sample has a given {@link CategoricalFeature}
     * @param feat the feature we want to know if is present
     * @return true in case the feature exists, false otherwise
     */
    public boolean hasFeature(CategoricalFeature<?> feat) {
        return features.stream().map(Feature::featureName).anyMatch(n -> n.equals(feat.featureName()));
    }

    /**
     * Returns an Optional containing the CategoricalFeature in this MixedSample with the same feature name
     * as the given CategoricalFeature, or an empty Optional if no matching feature is found.
     *
     * @param feat the CategoricalFeature to search for in this MixedSample
     * @return an Optional containing the matching CategoricalFeature, or an empty Optional if no matching feature is found
     */
    public Optional<CategoricalFeature<?>> feature(CategoricalFeature<?> feat) {
        return features.stream().filter(f -> f.featureName().equals(feat.featureName())).findFirst();
    }

    /**
     * Returns the number of categorical features that match the given MixedSample.
     *
     * @param another the MixedSample to compare against
     * @return the number of matching categorical features
     */
    public int numberOfMatches(MixedSample another) {

        return (int) another.features.stream()
                .filter(another::hasFeature)
                .map(a -> feature(a).filter(thisFeat -> thisFeat.matches(a)))
                .map(Optional::isPresent)
                .filter(b -> b)
                .count();


    }

    /**
     * Returns the number of features that exist in both this MixedSample and the given MixedSample.
     *
     * @param another the MixedSample to compare against
     * @return the number of matching features
     */
    public int numberOfMatchingFeatures(MixedSample another) {
        // Use stream to count the number of features that exist in both MixedSamples
        return (int) another.getFeatures().stream()
                .filter(another::hasFeature)
                .count();
    }
}