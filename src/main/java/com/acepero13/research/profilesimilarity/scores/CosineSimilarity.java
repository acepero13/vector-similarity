package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.Vector;

public class CosineSimilarity implements Similarity {
    @Override
    public Double similarityScore(Vector vectorizable, Vector another) {
        return vectorizable.cosine(another);
    }
}
