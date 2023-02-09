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

    public static Feature<Double> doubleFeature(double value, String name) {
        return new DoubleFeature(value, name, DEFAULT_WEIGHT);
    }

    private static abstract class AbstractFeature<T> implements Feature<T> {
        private final T value;
        private final String name;
        private final double weight;

        public AbstractFeature(T value, String featureName, double weight) {
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
            return toDouble();
        }

        protected abstract double toDouble();

        @Override
        public T originalValue() {
            return value;
        }

        @Override
        public String featureName() {
            return this.name;
        }
    }

    private static class DoubleFeature extends AbstractFeature<Double> {


        public DoubleFeature(Double value, String featureName, double weight) {
            super(value, featureName, weight);
        }

        @Override
        protected double toDouble() {
            return originalValue();
        }
    }

    private static class IntegerFeature extends AbstractFeature<Integer> {


        public IntegerFeature(Integer value, String featureName, double weight) {
            super(value, featureName, weight);
        }

        @Override
        protected double toDouble() {
            return originalValue().doubleValue();
        }
    }


    private static class BooleanFeature extends AbstractFeature<Boolean> {


        public BooleanFeature(Boolean value, String featureName, double weight) {
            super(value, featureName, weight);
        }

        @Override
        protected double toDouble() {
            return originalValue() ? 1.0 : 0.0;

        }
    }
}
