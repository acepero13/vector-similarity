package com.acepero13.research.profilesimilarity.core.classifier.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A class representing a prediction result, consisting of a predicted value and a score.
 * The score does not represent a probability
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class Prediction {
    /**
     * The predicted value.
     */

    private final double prediction;
    /**
     * The score for the prediction.
     */
    private final double score;
}
