package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.*;

class CombinedSimilarityTest {

    private final Similarity scorer = new CombinedSimilarity();

    @Test
    void computeSimilarity() {
        var v1 = Vector.of(0, 1, 0, 0, 0, 1, 0, 0, 1);
        var v2 = Vector.of(0, 0, 1, 0, 0, 0, 0, 0, 1);

        var vec1 = new VectorWrapper(v1);
        var vec2 = new VectorWrapper(v2);

        double score = scorer.similarityScore(vec1, vec2);

        assertThat(score, closeTo(0.32, 0.01));
    }
}