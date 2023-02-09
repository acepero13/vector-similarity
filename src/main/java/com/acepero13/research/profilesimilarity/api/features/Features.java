package com.acepero13.research.profilesimilarity.api.features;

import com.acepero13.research.profilesimilarity.api.Feature;

public final class Features {

    public static final double DEFAULT_WEIGHT = 1.0;

    private Features() {
    }

    public static Feature<Boolean> booleanFeature(Boolean value, String name) {
        return new BooleanFeature(value, name, DEFAULT_WEIGHT);
    }

    public static Feature<Boolean> booleanFeature(Boolean value, String name, double weight) {
        return new BooleanFeature(value, name, weight);
    }

    public static Feature<Integer> integerFeature(Integer value, String name, double weight) {
        return new IntegerFeature(value, name, weight);
    }

    public static Feature<Integer> integerFeature(Integer value, String name) {
        return new IntegerFeature(value, name, DEFAULT_WEIGHT);
    }

    private static class IntegerFeature implements Feature<Integer> {

        private final int value;
        private final String name;
        private final double weight;

        public IntegerFeature(int value, String featureName, double weight) {
            this.value = value;
            this.name = featureName;
            this.weight = weight;
        }

        @Override
        public double weight() {
            return weight;
        }

        @Override
        public double featureValue() {
            return value;
        }

        @Override
        public Integer originalValue() {
            return value;
        }

        @Override
        public String featureName() {
            return this.name;
        }
    }


    private static class BooleanFeature implements Feature<Boolean> {

        private final Boolean value;
        private final String name;
        private final double weight;

        private BooleanFeature(Boolean value, String name, double weight) {
            this.value = value;
            this.name = name;
            this.weight = weight;
        }

        @Override
        public double featureValue() {
            return value ? 1.0 : 0.0;
        }

        @Override
        public double weight() {
            return weight;
        }

        @Override
        public Boolean originalValue() {
            return value;
        }

        @Override
        public String featureName() {
            return name;
        }
    }
}
