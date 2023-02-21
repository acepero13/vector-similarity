package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class CombinedMetricTest {

    private final Metric scorer = Metrics.normalizedEuclideanDistance();

    @Test
    void computeSimilarity() {
        var v1 = DoubleVector.of(0, 1, 0, 0, 0, 1, 0, 0, 1);
        var v2 = DoubleVector.of(0, 0, 1, 0, 0, 0, 0, 0, 1);

        var vec1 = new VectorWrapper(v1);
        var vec2 = new VectorWrapper(v2);

        double score = scorer.similarityScore(NormalizedVector.of(vec1.vector()), NormalizedVector.of(vec2.vector()));

        assertThat(score, closeTo(0.84, 0.01));
    }

    @Test
    void euclideanDistance() {
        var v1 = NormalizedVector.of(DoubleVector.of(1, 1, 1, 1));
        var v2 = NormalizedVector.of(DoubleVector.of(0, 0, 0, 0));

        double score = Metrics.euclideanDistance().similarityScore(v1, v2);
        assertThat(score, equalTo(2.0));
    }
}