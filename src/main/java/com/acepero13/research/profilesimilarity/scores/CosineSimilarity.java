package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;

public class CosineSimilarity implements Similarity {
    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {
        return vectorizable.cosine(another);
    }
}
