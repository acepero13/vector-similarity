package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;

import java.util.List;

public interface Vectorizable {
    DoubleVector vector();

    Vector<Double> vector(Normalizer normalizer);

    List<Feature<?>> whiteList();

    Vector<Double> vector(List<Feature<?>> whiteList);
}
