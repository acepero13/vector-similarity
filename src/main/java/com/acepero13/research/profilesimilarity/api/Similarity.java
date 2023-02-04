package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.Vectorizable;

public interface Similarity {
    Double similarityScore(Vectorizable vectorizable, Vectorizable another);
}
