package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.annotations.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.result.Classification;
import com.acepero13.research.profilesimilarity.core.classifier.result.Prediction;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

class KnnTest {


    @Test
    void classificationTest() {
        var sample1 = new AcidDurability(7, 7, CLASSIFICATION.BAD);
        var sample2 = new AcidDurability(7, 4, CLASSIFICATION.BAD);
        var sample3 = new AcidDurability(3, 4, CLASSIFICATION.GOOD);
        var sample4 = new AcidDurability(1, 4, CLASSIFICATION.GOOD);


        var classifier = Knn.withDefaultNormalizer(3, sample1, sample2, sample3, sample4);

        var test = new AcidDurability(3, 7);

        CategoricalFeature<?> result = classifier.fit(test)
                .classify(CLASSIFICATION.class)
                ;

        assertThat(result, equalTo(CLASSIFICATION.GOOD));

    }

    @Test
    void classificationUsingAnnotation() {
        var sample1 = new AnnotatedAcidDurabilityWithTarget(7, 7, CLASSIFICATION_CAT.BAD);
        var sample2 = new AnnotatedAcidDurabilityWithTarget(7, 4, CLASSIFICATION_CAT.BAD);
        var sample3 = new AnnotatedAcidDurabilityWithTarget(3, 4, CLASSIFICATION_CAT.GOOD);
        var sample4 = new AnnotatedAcidDurabilityWithTarget(1, 4, CLASSIFICATION_CAT.GOOD);


        var classifier = Knn.ofObjectsWithDefaultNormalizer(3, List.of(sample1, sample2, sample3, sample4));

        var test = new AnnotatedAcidDurabilityWithTarget(3, 7, null);

        Classification result = classifier.fit(test).classifyWithScore();

        assertThat(result.classification(), equalTo(CLASSIFICATION_CAT.GOOD));
        assertThat(result.probability().value(), closeTo(0.67, 0.1));
    }

    @Test
    void predictionUsingAnnotation() {
        var sample1 = new AnnotatedAcidDurabilityPrediction(7, 7, CLASSIFICATION_CAT.BAD);
        var sample2 = new AnnotatedAcidDurabilityPrediction(7, 4, CLASSIFICATION_CAT.BAD);
        var sample3 = new AnnotatedAcidDurabilityPrediction(3, 4, CLASSIFICATION_CAT.GOOD);
        var sample4 = new AnnotatedAcidDurabilityPrediction(1, 4, CLASSIFICATION_CAT.GOOD);


        var classifier = Knn.ofObjectsWithDefaultNormalizer(3, List.of(sample1, sample2, sample3, sample4));

        var test = new AnnotatedAcidDurabilityPrediction(null, 7, CLASSIFICATION_CAT.BAD);

        Prediction result = classifier.fit(test).predictWithScore();

        assertThat(result.prediction(), closeTo(5.6, 0.1));
        assertThat(result.score(), closeTo(0.5, 0.1));
    }

    public enum CLASSIFICATION_CAT {
        GOOD, BAD, UNKNOWN;
    }

    private enum CLASSIFICATION implements CategoricalFeature<CLASSIFICATION> {
        GOOD, BAD, UNKNOWN;


        @Override
        public CLASSIFICATION originalValue() {
            return this;
        }

        @Override
        public String featureName() {
            return "classification";
        }

        @Override
        public boolean isTarget() {
            return true;
        }
    }


    @Data
    @Vectorizable
    public static class AnnotatedAcidDurabilityWithTarget {
        @Numerical
        private final Integer durabilitySeconds;
        @Numerical
        private final int strengthKgSqM;
        @Categorical(target = true, name = "classification-cat")
        private final CLASSIFICATION_CAT classification;
    }

    @Data
    @Vectorizable
    public static class AnnotatedAcidDurabilityPrediction {
        @Numerical(target = true, name = "durability-seconds")
        private final Integer durabilitySeconds;
        @Numerical
        private final int strengthKgSqM;
        @Categorical(name = "classification-cat")
        private final CLASSIFICATION_CAT classification;


    }

    private static class AcidDurability extends AbstractVectorizable {
        private final int durabilitySeconds;
        private final int strengthKgSqM;
        private final CLASSIFICATION classification;

        private AcidDurability(int durabilitySeconds, int strengthKgSqM, CLASSIFICATION classification) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.classification = classification;
            this.addNonNullFeature(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"))
                    .addNonNullFeature(Features.integerFeature(strengthKgSqM, "Strength in km/m2"))
                    .addNonNullFeature(classification);
        }


        public AcidDurability(int durabilitySeconds, int strengthKgSqM) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.addNonNullFeature(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"))
                    .addNonNullFeature(Features.integerFeature(strengthKgSqM, "Strength in km/m2"));
            this.classification = CLASSIFICATION.UNKNOWN;
        }


    }
}

