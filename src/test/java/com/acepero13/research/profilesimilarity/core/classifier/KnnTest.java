package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.annotations.Categorical;
import com.acepero13.research.profilesimilarity.annotations.Numerical;
import com.acepero13.research.profilesimilarity.annotations.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
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
                .classify(CLASSIFICATION.class);

        assertThat(result, equalTo(CLASSIFICATION.GOOD));

    }

    @Test
    void classificationUsingAnnotation() {
        var sample1 = new AnnotatedAcidDurability(7, 7, CLASSIFICATION.BAD);
        var sample2 = new AnnotatedAcidDurability(7, 4, CLASSIFICATION.BAD);
        var sample3 = new AnnotatedAcidDurability(3, 4, CLASSIFICATION.GOOD);
        var sample4 = new AnnotatedAcidDurability(1, 4, CLASSIFICATION.GOOD);


        var classifier = Knn.ofObjectsWithDefaultNormalizer(3, List.of(sample1, sample2, sample3, sample4));

        var test = new AnnotatedAcidDurability(3, 7, null);

        CategoricalFeature<?> result = classifier.fit(test)
                .classify(CLASSIFICATION.class);

        assertThat(result, equalTo(CLASSIFICATION.GOOD));
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
    }

    @Data
    @Vectorizable
    private static class AnnotatedAcidDurability {
        @Numerical
        private final int durabilitySeconds;
        @Numerical
        private final int strengthKgSqM;
        @Categorical
        private final CLASSIFICATION classification;


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

