package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.Objects;

/**
 * The Score class represents a score associated with a sample. A score is typically the result
 * <p>
 * of evaluating some model on a given sample, and the sample is a vectorizable representation
 * <p>
 * of the input to the model.
 */
public class Score {

    /**
     * The score value.
     */
    private final double score;
    /**
     * The sample associated with the score.
     */
    private final FeatureVector sample;

    /**
     * Constructs a new Score object with the specified score value and sample.
     *
     * @param score  the score value
     * @param sample the sample associated with the score
     */
    public Score(double score, FeatureVector sample) {
        this.score = score;
        this.sample = Objects.requireNonNull(sample);
    }

    /**
     * Returns the sample associated with the score.
     *
     * @return the sample associated with the score
     */
    public FeatureVector sample() {
        return sample;
    }

    /**
     * Returns the score value.
     *
     * @return the score value
     */
    public double score() {
        return score;
    }
}
