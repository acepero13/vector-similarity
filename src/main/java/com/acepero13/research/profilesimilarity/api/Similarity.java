package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.NormalizedVector;

public interface Similarity {
    Double similarityScore(NormalizedVector vectorizable, NormalizedVector another);
}
