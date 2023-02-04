package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

public class CosineSimilarity implements Similarity {
    @Override
    public Double similarityScore(Vectorizable vectorizable, Vectorizable another) {
        return vectorizable.vector().cosine(another.vector());
    }
}
