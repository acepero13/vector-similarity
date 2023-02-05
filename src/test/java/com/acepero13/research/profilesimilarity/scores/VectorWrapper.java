package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class VectorWrapper implements Vectorizable {

    private final Vector vector;
    private List<UnaryOperator<Double>> normalizer = new ArrayList<>();

    public VectorWrapper(Vector vector) {
        this.vector = vector;
    }

    @Override
    public Vector vector() {
        return vector.normalizeWith(normalizer);
    }

    @Override
    public void setNormalizer(List<UnaryOperator<Double>> normalizer) {
        this.normalizer = normalizer;
    }

}
