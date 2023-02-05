package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Vector;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.UnaryOperator;

@Data
@Builder
public class UserProfile implements Vectorizable {

    private final boolean likesToBuyEcoProducts;
    private final boolean isInterestedInEcoProducts;
    private final boolean drivesInEcoMode;
    private final double percentageOfEcoFriendlyProductsBought;
    private final Gender gender;
    private final int numberOfChildren;
    private final int salary;
    private final String name;

    private List<UnaryOperator<Double>> normalizer;

    @Override
    public Vector vector() {
        return Vector.of(
                Feature.booleanFeature(drivesInEcoMode, "user drives in eco mode"),
                Feature.booleanFeature(isInterestedInEcoProducts, "user is interested in eco-friendly products"),
                Feature.booleanFeature(likesToBuyEcoProducts, "user likes to buy eco-friendly products"),
                gender,
                Feature.integerFeature(salary, "salary"),
                Feature.integerFeature(numberOfChildren, "childrens")
        );
    }

    @Override
    public Vector vector(Normalizer normalizer) {
        return normalizer.normalize(vector());
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
