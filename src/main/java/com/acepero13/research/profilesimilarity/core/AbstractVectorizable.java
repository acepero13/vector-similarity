package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public abstract class AbstractVectorizable implements Vectorizable {

    private final List<Feature<?>> features = new ArrayList<>();

    public AbstractVectorizable addNonNullFeature(Feature<?> value) {
        if (value != null &&  value.originalValue() != null) {
            this.features.add(value);
        }
        return this;
    }

    public <T extends CategoricalFeature<?>> AbstractVectorizable addAsOneHotEncodingFeature(
            OneHotEncodingExtractor<T> extractor,
            List<T> elements) {

        List<Feature<?>> oneHotEncodingList = requireNonNull(extractor).convert(requireNonNull(elements));
        this.features.addAll(requireNonNull(oneHotEncodingList));
        return this;
    }

    @Override
    public Vector<Double> vector() {
        return DoubleVector.ofFeatures(features);
    }

    @Override
    public List<Feature<?>> features() {
        return features;
    }


}
