package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.NormalizedVector;

public interface Metric {
    Double similarityScore(NormalizedVector vectorizable, NormalizedVector another);
}
