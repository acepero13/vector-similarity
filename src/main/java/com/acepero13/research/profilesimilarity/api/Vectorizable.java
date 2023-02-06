package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;

public interface Vectorizable {
    DoubleVector vector();

    Vector<Double> vector(Normalizer normalizer);
}
