package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Data
@Builder
public class UserProfile implements Vectorizable {
    private final boolean userLikesToBuyEcoProducts;
    private final boolean userIsInterestedInEcoProducts;
    private final boolean userDrivesInEcoMode;
    private final double percentageOfEcoFriendlyProductsBought;
    private final Gender gender;
    private List<UnaryOperator<Double>> normalizer;

    @Override
    public Vector vector() {
        if(normalizer == null) {
            normalizer = new ArrayList<>();
        }
        return Vector.of(
                Feature.booleanFeature(userDrivesInEcoMode, "user drives in eco mode"),
                Feature.booleanFeature(userIsInterestedInEcoProducts, "user is interested in eco-friendly products"),
                Feature.booleanFeature(userLikesToBuyEcoProducts, "user likes to buy eco-friendly products"),
                gender).normalizeWith(normalizer);
    }

    @Override
    public void setNormalizer(List<UnaryOperator<Double>> normalizer) {
        this.normalizer = normalizer;
    }

    enum Gender implements Feature<Gender> {
        FEMALE(0), MALE(1);


        private final double value;

        Gender(int value) {
            this.value = value;
        }

        @Override
        public double featureValue() {
            return value;
        }

        @Override
        public Gender originalValue() {
            return this;
        }
    }
}
