package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.WithCategoricalLabel;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.api.CategoricalLabel;
import com.acepero13.research.profilesimilarity.core.AbstractVectorizable;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.api.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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


        var classifier = new Knn(3, sample1, sample2, sample3, sample4);
        var target = new AcidDurability(3, 7);
        CategoricalLabel<CLASSIFICATION> result = classifier.classify(target);
        assertThat(result.value(), equalTo(CLASSIFICATION.GOOD));

    }

    private enum CLASSIFICATION {
        GOOD, BAD, UNKNOWN;
    }


    private static class AcidDurability extends AbstractVectorizable implements WithCategoricalLabel {
        private final int durabilitySeconds;
        private final int strengthKgSqM;
        private final CLASSIFICATION classification;

        private AcidDurability(int durabilitySeconds, int strengthKgSqM, CLASSIFICATION classification) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.classification = classification;
            this.addNonNullFeature(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"))
                .addNonNullFeature(Features.integerFeature(strengthKgSqM, "Strength in km/m2"));
        }


        public AcidDurability(int durabilitySeconds, int strengthKgSqM) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.addNonNullFeature(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"))
                .addNonNullFeature(Features.integerFeature(strengthKgSqM, "Strength in km/m2"));
            this.classification = CLASSIFICATION.UNKNOWN;
        }


        @Override
        public CategoricalLabel<CLASSIFICATION> label() {
            return CategoricalLabel.defaultLabel(classification);
        }
    }
}

