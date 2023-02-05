package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;

import java.util.List;

public class CombinedSimilarity implements Similarity {
    private static final List<Similarity> similarityScorers = List.of(new CosineSimilarity(), new EuclideanDistance());

    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {

        Double score = similarityScorers.stream()
                .map(s -> s.similarityScore(vectorizable, another))
                .reduce(0.0, Double::sum);

        return score / (double) similarityScorers.size();


    }
}
