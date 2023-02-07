package com.acepero13.research.profilesimilarity;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.DoubleVector;
import com.acepero13.research.profilesimilarity.core.Vector;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Data
public class UserProfile implements Vectorizable {

    private final Boolean likesToBuyEcoProducts;
    private final Boolean isInterestedInEcoProducts;
    private final Boolean drivesInEcoMode;
    private final Double percentageOfEcoFriendlyProductsBought;
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
        this.percentageOfEcoFriendlyProductsBought = builder.percentageOfEcoFriendlyProductsBought;
        this.gender = builder.gender;
        this.numberOfChildren = builder.numberOfChildren;
        this.salary = builder.salary;
        this.name = builder.name;


        if (drivesInEcoMode != null) {
            features.add(Feature.booleanFeature(drivesInEcoMode, "user drives in eco mode"));
        }
        if (isInterestedInEcoProducts != null) {
            features.add(Feature.booleanFeature(isInterestedInEcoProducts, "user is interested in eco-friendly products"));
        }
        if (likesToBuyEcoProducts != null) {
            features.add(Feature.booleanFeature(likesToBuyEcoProducts, "user likes to buy eco-friendly products"));
        }
        if (gender != null) {
            features.add(gender);
        }
        if (salary != null) {
            features.add(Feature.integerFeature(salary, "salary"));
        }
        if (numberOfChildren != null) {
            features.add(Feature.integerFeature(numberOfChildren, "childrens"));
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public DoubleVector vector() {
        return DoubleVector.ofFeatures(features);


    }

    @Override
    public Vector<Double> vector(Normalizer normalizer) {
        return normalizer.normalize(vector());
    }

    @Override
    public List<Feature<?>> whiteList() {
        return features;
    }

    @Override
    public Vector<Double> vector(List<Feature<?>> whiteList) {
        if(whiteList.isEmpty()) {
            return vector();
        }
        return DoubleVector.ofFeatures(features.stream().filter(f -> f.isWhiteListed(whiteList))
                .collect(Collectors.toList()));
    }

    public static class Builder {
        private Boolean likesToBuyEcoProducts;
        private Boolean isInterestedInEcoProducts;
        private Boolean drivesInEcoMode;
        private Double percentageOfEcoFriendlyProductsBought;
        private Gender gender;
        private Integer numberOfChildren;
        private Integer salary;
        private String name;

        public boolean likesToBuyEcoProducts() {
            return likesToBuyEcoProducts;
        }

        public Builder likesToBuyEcoProducts(boolean likesToBuyEcoProducts) {
            this.likesToBuyEcoProducts = likesToBuyEcoProducts;
            return this;
        }

        public boolean isInterestedInEcoProducts() {
            return isInterestedInEcoProducts;
        }

        public Builder isInterestedInEcoProducts(boolean interestedInEcoProducts) {
            isInterestedInEcoProducts = interestedInEcoProducts;
            return this;
        }

        public boolean drivesInEcoMode() {
            return drivesInEcoMode;
        }

        public Builder drivesInEcoMode(boolean drivesInEcoMode) {
            this.drivesInEcoMode = drivesInEcoMode;
            return this;
        }

        public double percentageOfEcoFriendlyProductsBought() {
            return percentageOfEcoFriendlyProductsBought;
        }

        public Builder percentageOfEcoFriendlyProductsBought(double percentageOfEcoFriendlyProductsBought) {
            this.percentageOfEcoFriendlyProductsBought = percentageOfEcoFriendlyProductsBought;
            return this;
        }

        public Gender gender() {
            return gender;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public int numberOfChildren() {
            return numberOfChildren;
        }

        public Builder numberOfChildren(int numberOfChildren) {
            this.numberOfChildren = numberOfChildren;
            return this;
        }

        public int salary() {
            return salary;
        }

        public Builder salary(int salary) {
            this.salary = salary;
            return this;
        }

        public String name() {
            return name;
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

        @Override
        public String featureName() {
            return "gender";
        }
    }
}
