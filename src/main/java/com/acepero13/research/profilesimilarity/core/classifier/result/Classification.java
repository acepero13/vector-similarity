package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A class representing a classification result, consisting of a categorical feature
 * and a probability value.
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class Classification {
    /**
     * The categorical feature representing the classification.
     */
    CategoricalFeature<?> classification;
    /**
     * The probability value for the classification.
     */
    Probability probability;
}
