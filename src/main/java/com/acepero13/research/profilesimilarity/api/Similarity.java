package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Vector;

public interface Similarity {
    Double similarityScore(Vector vectorizable, Vector another);
}
