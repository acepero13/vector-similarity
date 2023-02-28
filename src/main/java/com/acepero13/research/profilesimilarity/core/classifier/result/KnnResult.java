package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;
import java.util.function.Predicate;

/**
 * The {@code KnnResult} interface provides a contract for the results of a k-nearest neighbors (k-NN) classification
 * <p>
 * or regression algorithm.
 *
 * @since 1.0
 */
public interface KnnResult {

    /**
     * Returns a new instance of {@code KnnResult} containing the provided list of feature vectors.
     *
     * @param vectors the list of feature vectors to include in the result.
     * @return a new instance of {@code KnnResult} containing the provided list of feature vectors.
     */
    static KnnResult of(List<FeatureVector> vectors) {
        return new FeatureVectorResult(vectors);
    }

    /**
     * Returns a categorical feature that represents the predicted class of the input feature vector. The feature name
     * is specified as a string parameter.
     *
     * @param featureName the name of the feature to classify.
     * @return a categorical feature that represents the predicted class of the input feature vector.
     */
    CategoricalFeature<?> classify(String featureName);

    /**
     * Returns a categorical feature that represents the predicted class of the input feature vector. The feature type
     * is specified as a {@code Class} parameter.
     *
     * @param type the class of the feature to classify.
     * @return a categorical feature that represents the predicted class of the input feature vector.
     */
    CategoricalFeature<?> classify(Class<? extends CategoricalFeature<?>> type);

    /**
     * Returns a the classification result that represents the predicted class and a score of the classification of the input feature vector. The feature type
     * is specified as a {@code Class} parameter.
     *
     * @param type the class of the feature to classify.
     * @return Classification that represents the predicted class of the input feature vector and a score.
     */
    Classification classifyWithScore(Class<? extends CategoricalFeature<?>> type);

    /**
     * Returns a numerical feature that represents the predicted value of the input feature vector. The feature name
     * is specified as a string parameter.
     *
     * @param featureName the name of the feature to predict.
     * @return a numerical feature that represents the predicted value of the input feature vector.
     */
    Double predict(String featureName);

    /**
     * Returns a list of categorical features that represent the predicted classes of the input feature vector,
     * using a one-hot encoding scheme. The method takes a {@code Predicate} parameter that specifies which features
     * to include in the result.
     *
     * @param featureNameMatcher a predicate that specifies which feature names to include in the result.
     * @return a list of categorical features that represent the predicted classes of the input feature vector.
     * @throws UnsupportedOperationException if the method is not supported by the implementing class.
     */
    default List<CategoricalFeature<?>> classifyOneHot(Predicate<String> featureNameMatcher) {
        throw new UnsupportedOperationException("classification for one hot vectors is not supported");
    }
}
