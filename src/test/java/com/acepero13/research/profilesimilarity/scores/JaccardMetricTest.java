package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class JaccardMetricTest {
    private final Metric metricScorer = new JaccardMetric();

    @Test
    void similarity() {
        var v1 = DoubleVector.of(0, 1, 0, 0, 0, 1, 0, 0, 1);
        var v2 = DoubleVector.of(0, 0, 1, 0, 0, 0, 0, 0, 1);

        var vec1 = new VectorWrapper(v1);
        var vec2 = new VectorWrapper(v2);

        double score = metricScorer.similarityScore(NormalizedVector.of(vec1.vector()), NormalizedVector.of(vec2.vector()));

        assertThat(score, closeTo(0.25, 0.01));


    }

}