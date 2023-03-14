package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * This abstract class provides a base implementation of the Vectorizable interface
 * <p>
 * and provides methods for adding features to the feature list.
 */

public abstract class AbstractVectorizable implements Vectorizable {

    private final List<Feature<?>> features = new ArrayList<>();

    /**
     * Adds a non-null feature to the feature list.
     *
     * @param value the feature to add.
     * @return the updated AbstractVectorizable object.
     */
    public AbstractVectorizable addNonNullFeature(Feature<?> value) {
        if (value != null && value.originalValue() != null) {
            this.features.add(value);
        }
        return this;
    }

    /**
     * Adds a list of elements as a one-hot encoded feature to the feature list using the provided extractor.
     *
     * @param extractor the OneHotEncodingExtractor to use for encoding the elements.
     * @param elements  the list of elements to encode.
     * @param <T>       the type of the categorical feature.
     * @return the updated AbstractVectorizable object.
     */
    public <T extends CategoricalFeature<?>> AbstractVectorizable addAsOneHotEncodingFeature(
            OneHotEncodingExtractor<T> extractor,
            List<T> elements) {

        List<Feature<?>> oneHotEncodingList = requireNonNull(extractor).convert(requireNonNull(elements));
        this.features.addAll(requireNonNull(oneHotEncodingList));
        return this;
    }


    /**
     * Adds a list of elements as a one-hot encoded feature to the feature list using the provided array of all possible elements.
     *
     * @param allPossibleElements an array of all possible elements for the categorical feature.
     * @param elements            the list of elements to encode.
     * @param <T>                 the type of the categorical feature.
     * @return the updated AbstractVectorizable object.
     */
    public <T extends CategoricalFeature<?>> AbstractVectorizable addAsOneHotEncodingFeature(
            T[] allPossibleElements, List<T> elements) {
        addAsOneHotEncodingFeature(OneHotEncodingExtractor.oneHotEncodingOf(requireNonNull(allPossibleElements)), requireNonNull(elements));
        return this;
    }

    /**
     * Returns a vector representation of the features.
     *
     * @return a vector representation of the features.
     */
    @Override
    public Vector<Double> vector() {
        return DoubleVector.ofFeatures(features);
    }

    /**
     * Returns a list of the features.
     *
     * @return a list of the features.
     */
    @Override
    public List<Feature<?>> features() {
        return features;
    }


}
