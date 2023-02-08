package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.api.features.Features;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@Data
public class UserProfile implements Vectorizable {

    private final Boolean likesToBuyEcoProducts;
    private final Boolean isInterestedInEcoProducts;
    private final Boolean drivesInEcoMode;
    private final Gender gender;
    private final Integer numberOfChildren;
    private final Integer salary;
    private final String name;
    private final List<Feature<?>> features = new ArrayList<>();
    private final List<Feature<?>> exclusions = new ArrayList<>();

    private List<UnaryOperator<Double>> normalizer;

    public UserProfile(Builder builder) {
        this.likesToBuyEcoProducts = builder.likesToBuyEcoProducts;
        this.isInterestedInEcoProducts = builder.isInterestedInEcoProducts;
        this.drivesInEcoMode = builder.drivesInEcoMode;
        this.gender = builder.gender;
        this.numberOfChildren = builder.numberOfChildren;
        this.salary = builder.salary;
        this.name = builder.name;


        if (drivesInEcoMode != null) {
            features.add(Features.booleanFeature(drivesInEcoMode, "user drives in eco mode"));
        }
        if (isInterestedInEcoProducts != null) {
            features.add(Features.booleanFeature(isInterestedInEcoProducts, "user is interested in eco-friendly products"));
        }
        if (likesToBuyEcoProducts != null) {
            features.add(Features.booleanFeature(likesToBuyEcoProducts, "user likes to buy eco-friendly products"));
        }
        if (gender != null) {
            features.add(gender);
        }
        if (salary != null) {
            features.add(Features.integerFeature(salary, "salary", 0.5));
        }
        if (numberOfChildren != null) {
            features.add(Features.integerFeature(numberOfChildren, "childrens"));
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Vector<Double> vector() {
        return DoubleVector.ofFeatures(features);

    }

    @Override
    public List<Feature<?>> features() {
        return features;
    }

    public static class Builder {
        private Boolean likesToBuyEcoProducts;
        private Boolean isInterestedInEcoProducts;
        private Boolean drivesInEcoMode;
        private Gender gender;
        private Integer numberOfChildren;
        private Integer salary;
        private String name;


        public Builder likesToBuyEcoProducts(boolean likesToBuyEcoProducts) {
            this.likesToBuyEcoProducts = likesToBuyEcoProducts;
            return this;
        }


        public Builder isInterestedInEcoProducts(boolean interestedInEcoProducts) {
            isInterestedInEcoProducts = interestedInEcoProducts;
            return this;
        }


        public Builder drivesInEcoMode(boolean drivesInEcoMode) {
            this.drivesInEcoMode = drivesInEcoMode;
            return this;
        }


        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }


        public Builder numberOfChildren(int numberOfChildren) {
            this.numberOfChildren = numberOfChildren;
            return this;
        }


        public Builder salary(int salary) {
            this.salary = salary;
            return this;
        }


        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(this);
        }
    }

    enum Gender implements Feature<Gender> {
        FEMALE(1), MALE(0);


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

        @Override
        public String featureName() {
            return "gender";
        }
    }
}
