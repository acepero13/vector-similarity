package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

public interface Metric {
    Double similarityScore(NormalizedVector vectorizable, NormalizedVector another);
}
