package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile implements Vectorizable {
    private final boolean userLikesToBuyEcoProducts;
    private final boolean userIsInterestedInEcoProducts;
    private final boolean userDrivesInEcoMode;
    private final Gender gender;

    @Override
    public Vector vector() {
        return Vector.of(
                Feature.booleanFeature(userDrivesInEcoMode),
                Feature.booleanFeature(userIsInterestedInEcoProducts),
                Feature.booleanFeature(userLikesToBuyEcoProducts),
                gender);
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
