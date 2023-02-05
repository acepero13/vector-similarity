package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.Vector;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Vectorizable {
    Vector vector();


    //TODO: This is a code smell
    void setNormalizer(List<UnaryOperator<Double>> minMaxNormalization);
}
