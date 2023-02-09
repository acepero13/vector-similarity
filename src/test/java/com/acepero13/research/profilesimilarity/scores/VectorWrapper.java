package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

import java.util.ArrayList;
import java.util.List;

public class VectorWrapper implements Vectorizable {

    private final DoubleVector vector;

    public VectorWrapper(DoubleVector vector) {
        this.vector = vector;
    }

    @Override
    public Vector<Double> vector() {
        return vector;
    }

    @Override
    public List<Feature<?>> features() {
        return new ArrayList<>();
    }

    @Override
    public Vector<Double> vector(List<Feature<?>> whiteList) {
        return vector;
    }

    @Override
    public Feature<?> targetFeature() {
        return Features.booleanFeature(true, "test");
    }


}
