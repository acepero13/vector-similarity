package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;

import java.util.List;
import java.util.stream.Collectors;

public interface Vectorizable {
    Vector<Double> vector();


    List<Feature<?>> features();

    default Vector<Double> vector(List<Feature<?>> whiteList) {
        if (whiteList.isEmpty()) {
            return vector();
        }
        return DoubleVector.ofFeatures(features().stream().filter(f -> f.isWhiteListed(whiteList))
                .collect(Collectors.toList()));
    }


}
