package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;

public interface KnnResult<T> {
    static KnnResult<?> of(List<FeatureVector> vectors) {
        return new FeatureVectorResult<>(vectors);
    }

    CategoricalFeature<T> classify(String featureName);
    CategoricalFeature<T> classify(Class<? extends CategoricalFeature<?>> type);

    Double predict(String age);
}
