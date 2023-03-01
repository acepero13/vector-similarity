package com.acepero13.research.profilesimilarity.core.classifier.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class Prediction {
    private final double prediction;
    private final double score;
}
