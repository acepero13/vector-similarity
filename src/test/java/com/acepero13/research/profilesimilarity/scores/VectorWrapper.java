package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class VectorWrapper implements Vectorizable {

    private final DoubleVector vector;
    private List<UnaryOperator<Double>> normalizer = new ArrayList<>();

    public VectorWrapper(DoubleVector vector) {
        this.vector = vector;
    }

    @Override
    public DoubleVector vector() {
        return vector;
    }

    @Override
    public Vector<Double> vector(Normalizer normalizer) {
        return normalizer.normalize(vector);
    }


}
