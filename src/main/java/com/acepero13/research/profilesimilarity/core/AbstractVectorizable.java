package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

import java.util.ArrayList;
import java.util.List;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public abstract class AbstractVectorizable implements Vectorizable {

    private final List<Feature<?>> features = new ArrayList<>();

    public AbstractVectorizable addNonNullFeature(Feature<?> value) {
        if (value.originalValue() != null) {
            this.features.add(value);
        }
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
