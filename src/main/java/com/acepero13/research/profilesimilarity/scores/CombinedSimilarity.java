package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

public class CombinedSimilarity implements Similarity {
    private final Similarity cosineSimilarity = new CosineSimilarity();
    private final Similarity jaccardSimilarity = new JaccardSimilarity();

    @Override
    public Double similarityScore(Vectorizable vectorizable, Vectorizable another) {
        double cosine = cosineSimilarity.similarityScore(vectorizable, another);
        double jaccard = jaccardSimilarity.similarityScore(vectorizable, another);
        return (cosine + jaccard) / 2;
    }
}
