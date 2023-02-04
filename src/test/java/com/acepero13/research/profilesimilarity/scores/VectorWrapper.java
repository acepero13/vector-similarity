package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

public class VectorWrapper implements Vectorizable {

    private final Vector vector;

    VectorWrapper(Vector vector) {
        this.vector = vector;
    }

    @Override
    public Vector vector() {
        return vector;
    }
}
