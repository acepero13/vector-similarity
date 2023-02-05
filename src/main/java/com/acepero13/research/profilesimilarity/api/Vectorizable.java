package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Vector;

public interface Vectorizable {
    Vector vector();

    Vector vector(Normalizer normalizer);
}
