package com.acepero13.research.profilesimilarity.api.features;

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

    public static Feature<Boolean> categoricalBoolean(Boolean value, String name) {
        return new CategoricalFeature<>() {
            @Override
            public Boolean originalValue() {
                return value;
            }

            @Override
            public String featureName() {
                return name;
            }
        };
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


    private static class DoubleFeature extends AbstractNumericalFeature<Double> {


        public DoubleFeature(Double value, String featureName, double weight) {
            super(value, featureName, weight);
        }

        @Override
        protected Double toDouble() {
            return originalValue();
        }
    }

    private static class IntegerFeature extends AbstractNumericalFeature<Integer> {


        public IntegerFeature(Integer value, String featureName, double weight) {
            super(value, featureName, weight);
        }

        @Override
        protected Double toDouble() {
            return originalValue().doubleValue();
        }
    }


    private static class BooleanFeature implements Feature<Boolean> {


        private final Boolean value;
        private final String featureName;
        private final double weight;

        public BooleanFeature(Boolean value, String featureName, double weight) {
            // super(value, featureName, weight);
            this.value = value;
            this.featureName = featureName;
            this.weight = weight;
        }

        @Override
        public double weight() {
            return weight;
        }

        @Override
        public double featureValue() {
            return value ? 1.0 : 0.0;
        }

        @Override
        public Boolean originalValue() {
            return value;
        }

        @Override
        public String featureName() {
            return featureName;
        }
    }
}
