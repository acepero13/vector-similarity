package com.acepero13.research.profilesimilarity.api.features;

public interface CategoricalFeature<T> extends Feature<T> {
    @Override
    default double featureValue() {
       throw new UnsupportedOperationException("Categorical feature cannot be converted to double");
    }
}
