package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;

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


}
