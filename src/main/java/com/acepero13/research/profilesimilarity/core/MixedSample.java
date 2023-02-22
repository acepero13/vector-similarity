package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import lombok.Data;

import java.util.List;

@Data
public class MixedSample {
    private final NormalizedVector vector;
    private final List<CategoricalFeature<?>> features;


    public static MixedSample of(NormalizedVector sample, List<CategoricalFeature<?>> features) {
        return new MixedSample(sample, features);
    }
}
