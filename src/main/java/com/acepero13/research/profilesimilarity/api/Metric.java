package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.VectorCollector;

import java.util.List;

public interface Metric {
    Double similarityScore(NormalizedVector vectorizable, NormalizedVector another);

    default Double similarityScore(MixedSample sample, MixedSample another) {
        return 0.0;
    }

    static NormalizedVector from(List<CategoricalFeature<?>> features) {
        return NormalizedVector.of(features.stream()
                                           .map(Feature::originalValue)
                                           .filter(Boolean.class::isInstance)
                                           .map(Boolean.class::cast)
                                           .map(v -> v
                                                     ? 1.0
                                                     : 0.0)
                                           .collect(VectorCollector.toVector()));
    }
}
