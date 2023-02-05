package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.Vector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class JaccardSimilarityTest {
    private final Similarity similarityScorer = new JaccardSimilarity();

    @Test
    void similarity() {
        var v1 = Vector.of(0, 1, 0, 0, 0, 1, 0, 0, 1);
        var v2 = Vector.of(0, 0, 1, 0, 0, 0, 0, 0, 1);

        var vec1 = new VectorWrapper(v1);
        var vec2 = new VectorWrapper(v2);

        double score = similarityScorer.similarityScore(vec1.vector(), vec2.vector());

        assertThat(score, closeTo(0.25, 0.01));


    }

}