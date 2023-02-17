package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;
import java.util.function.Predicate;

public interface KnnResult {
    static KnnResult of(List<FeatureVector> vectors) {
        return new FeatureVectorResult(vectors);
    }

    CategoricalFeature<?> classify(String featureName);
    CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type);

    Double predict(String featureName);
    default List<CategoricalFeature<?>> classifyOneHot(Predicate<String> featureNameMatcher){
        throw new UnsupportedOperationException("classification for one hot vectors is not supported");
    }
}
