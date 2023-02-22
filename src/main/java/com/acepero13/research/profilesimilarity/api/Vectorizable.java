package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;
import java.util.stream.Collectors;

public interface Vectorizable {
    Vector<Double> vector();


    List<Feature<?>> features();

    default FeatureVector toFeatureVector() {
        return FeatureVector.of(features());
    }

    default Vector<Double> vector(List<Feature<?>> whiteList) {
        if (whiteList.isEmpty()) {
            return vector();
        }
        return DoubleVector.ofFeatures(features().stream()
                .filter(f -> f.isWhiteListed(whiteList))
                .filter(f -> !(f instanceof CategoricalFeature))
                .collect(Collectors.toList()));
    }


    default List<Feature<?>> numericalFeatures() {
        return features().stream()
                .filter(f -> !(f instanceof CategoricalFeature))
                .collect(Collectors.toList());
    }
}
