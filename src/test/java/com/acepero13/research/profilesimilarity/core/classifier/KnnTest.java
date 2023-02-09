package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.scores.EuclideanDistance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class KnnTest {

    private static final boolean BAD = false;
    private static final boolean GOOD = true;

    @Test
    void classifiesItem() {
        var sample1 = new AcidDurability(7, 7, BAD);
        var sample2 = new AcidDurability(7, 4, BAD);
        var sample3 = new AcidDurability(3, 4, GOOD);
        var sample4 = new AcidDurability(1, 4, GOOD);


        var classifier = new Knn(NormalizedVector::distanceTo, Features.booleanFeature(false, "label"), 3, sample1, sample2, sample3, sample4);
        var target = new AcidDurability(3, 7);
        AcidDurability result = (AcidDurability) classifier.mostSimilarTo(target);
        assertThat(result.classification, equalTo(GOOD));

    }


    private static class AcidDurability implements Vectorizable {
        private final int durabilitySeconds;
        private final int strengthKgSqM;
        private boolean classification;
        private Feature<Boolean> targetFeature;
        private final List<Feature<?>> features = new ArrayList<>();

        private AcidDurability(int durabilitySeconds, int strengthKgSqM, boolean classification) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.classification = classification;
            this.features.add(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"));
            this.features.add(Features.integerFeature(strengthKgSqM, "Strength in km/m2"));
            targetFeature = Features.booleanFeature(classification, "label");
            this.features.add(targetFeature);

        }

        // TODO: Rethink about the label

        public AcidDurability(int durabilitySeconds, int strengthKgSqM) {
            this.durabilitySeconds = durabilitySeconds;
            this.strengthKgSqM = strengthKgSqM;
            this.features.add(Features.integerFeature(durabilitySeconds, "Acid Durability (s)"));
            this.features.add(Features.integerFeature(strengthKgSqM, "Strength in km/m2"));
        }

        @Override
        public Vector<Double> vector() {
            return DoubleVector.ofFeatures(features);
        }

        @Override
        public List<Feature<?>> features() {
            return features;
        }

        @Override
        public Feature<?> targetFeature() {
            return targetFeature;
        }
    }
}

