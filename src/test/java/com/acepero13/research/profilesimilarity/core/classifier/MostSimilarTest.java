package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.core.classifier.result.Classification;
import com.acepero13.research.profilesimilarity.core.classifier.result.Prediction;
import com.acepero13.research.profilesimilarity.core.classifier.result.Probability;
import com.acepero13.research.profilesimilarity.scores.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class MostSimilarTest {
    @Test void prediction(){
        var sample1 = new KnnTest.AnnotatedAcidDurabilityPrediction(7, 7, KnnTest.CLASSIFICATION_CAT.BAD);
        var sample2 = new KnnTest.AnnotatedAcidDurabilityPrediction(7, 4, KnnTest.CLASSIFICATION_CAT.BAD);
        var sample3 = new KnnTest.AnnotatedAcidDurabilityPrediction(3, 4, KnnTest.CLASSIFICATION_CAT.GOOD);
        var sample4 = new KnnTest.AnnotatedAcidDurabilityPrediction(1, 4, KnnTest.CLASSIFICATION_CAT.GOOD);


        var classifier = MostSimilar.ofObjects(Metrics.gowersMetricCosineAndDice(), List.of(sample1, sample2, sample3, sample4));

        var test = new KnnTest.AnnotatedAcidDurabilityPrediction(null, 7, KnnTest.CLASSIFICATION_CAT.BAD);

        Prediction result = classifier.resultOfMostSimilarTo(test).predictWithScore();

        assertThat(result.prediction(), closeTo(7.0, 0.1));
        assertThat(result.score(), closeTo(50, 0.1));
    }

    @Test void classification(){
        var sample1 = new KnnTest.AnnotatedAcidDurabilityWithTarget(7, 7, KnnTest.CLASSIFICATION_CAT.BAD);
        var sample2 = new KnnTest.AnnotatedAcidDurabilityWithTarget(7, 4, KnnTest.CLASSIFICATION_CAT.BAD);
        var sample3 = new KnnTest.AnnotatedAcidDurabilityWithTarget(3, 4, KnnTest.CLASSIFICATION_CAT.GOOD);
        var sample4 = new KnnTest.AnnotatedAcidDurabilityWithTarget(1, 4, KnnTest.CLASSIFICATION_CAT.GOOD);


        var classifier = MostSimilar.ofObjects(Metrics.gowersMetricCosineAndDice(), List.of(sample1, sample2, sample3, sample4));

        var test = new KnnTest.AnnotatedAcidDurabilityWithTarget(7, 7, null);

        Classification result = classifier.resultOfMostSimilarTo(test).classifyWithScore();

        assertThat(result.classification(), equalTo(KnnTest.CLASSIFICATION_CAT.BAD));
        assertThat(result.probability().asPercentage(), closeTo(Probability.of(50).asPercentage(), 0.1));
    }

}