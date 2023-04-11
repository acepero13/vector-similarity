package com.acepero13.research.profilesimilarity.api.features;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The {@code Features} class provides static utility methods for creating {@link Feature} instances.
 */
public final class Features {

    /**
     * The default weight for a feature.
     */
    public static final double DEFAULT_WEIGHT = 1.0;

    /**
     * Private constructor to prevent instantiation.
     */
    private Features() {
    }

    /**
     * Returns a new boolean {@link Feature} instance with the specified value, name and default weight.
     *
     * @param value the boolean value of the feature.
     * @param name  the name of the feature.
     * @return a new boolean {@link Feature} instance with the specified value, name and default weight.
     */
    public static Feature<Boolean> booleanFeature(Boolean value, String name) {
        return new BooleanFeature(value, name, DEFAULT_WEIGHT, false);
    }

    /**
     * Returns a new boolean {@link Feature} instance with the specified value, name and weight.
     *
     * @param value  the boolean value of the feature.
     * @param name   the name of the feature.
     * @param weight the weight of the feature.
     * @return a new boolean {@link Feature} instance with the specified value, name and weight.
     */
    public static Feature<Boolean> booleanFeature(Boolean value, String name, double weight) {
        return new BooleanFeature(value, name, weight, false);
    }

    /**
     * Returns a new categorical boolean {@link Feature} instance with the specified value and name.
     *
     * @param value the boolean value of the feature.
     * @param name  the name of the feature.
     * @return a new categorical boolean {@link Feature} instance with the specified value and name.
     */

    public static Feature<Boolean> categoricalBoolean(Boolean value, String name) {
        return new CategoricalBoolean(value, name);
    }

    /**
     * Returns a new integer {@link AbstractNumericalFeature} instance with the specified value, name and weight.
     *
     * @param value  the integer value of the feature.
     * @param name   the name of the feature.
     * @param weight the weight of the feature.
     * @return a new integer {@link AbstractNumericalFeature} instance with the specified value, name and weight.
     */
    public static AbstractNumericalFeature<Integer> integerFeature(Integer value, String name, double weight) {
        return new IntegerFeature(value, name, weight);
    }

    /**
     * Returns a new integer {@link AbstractNumericalFeature} instance with the specified value, name and default weight.
     *
     * @param value the integer value of the feature.
     * @param name  the name of the feature.
     * @return a new integer {@link AbstractNumericalFeature} instance with the specified value, name and default weight.
     */
    public static AbstractNumericalFeature<Integer> integerFeature(Integer value, String name) {
        return new IntegerFeature(value, name, DEFAULT_WEIGHT);
    }

    /**
     * Returns a new double {@link AbstractNumericalFeature} instance with the specified value, name and default weight.
     *
     * @param value the double value of the feature.
     * @param name  the name of the feature.
     * @return a new double {@link AbstractNumericalFeature} instance with the specified value, name and default weight.
     */
    public static AbstractNumericalFeature<Double> doubleFeature(double value, String name) {
        return new DoubleFeature(value, name, DEFAULT_WEIGHT, false);
    }

    /**
     * Creates a new {@link DoubleFeature} with the given {@code value}, {@code name}, and {@code weight}.
     *
     * @param value  the double value of the feature
     * @param name   the name of the feature
     * @param weight the weight of the feature
     * @return a new instance of {@link DoubleFeature}
     */
    public static AbstractNumericalFeature<Double> doubleFeature(double value, String name, double weight) {
        return new DoubleFeature(value, name, weight, false);
    }

    /**
     * Creates a new {@link DoubleFeature} with the given {@code value}, {@code name}, and {@code weight}.
     *
     * @param value  the double value of the feature
     * @param name   the name of the feature
     * @param weight the weight of the feature
     * @return a new instance of {@link DoubleFeature}
     */
    public static AbstractNumericalFeature<Double> doubleFeature(Double value, String name, double weight) {
        return new DoubleFeature(value, name, weight, false);
    }

    /**
     * Creates a new {@link DoubleFeature} with the given {@code value}, {@code name}, and {@code weight}.
     *
     * @param value  the double value of the feature
     * @param name   the name of the feature
     * @param weight the weight of the feature
     * @return a new instance of {@link DoubleFeature}
     */
    public static Feature<?> integerFeature(Integer value, String name, double weight, boolean isTarget) {
        return new IntegerFeature(value, name, weight, isTarget);
    }
    /**
     * Creates a new {@link DoubleFeature} with the given {@code value}, {@code name}, and {@code weight}.
     *
     * @param value  the double value of the feature
     * @param name   the name of the feature
     * @param weight the weight of the feature
     * @return a new instance of {@link DoubleFeature}
     */

    public static Feature<?> doubleFeature(Double value, String name, double weight, boolean isTarget) {
        return new DoubleFeature(value, name, weight, isTarget);
    }

    /**
     * Creates a new {@link DoubleFeature} with the given {@code value}, {@code name}, and {@code weight}.
     *
     * @param value  the double value of the feature
     * @param name   the name of the feature
     * @param weight the weight of the feature
     * @return a new instance of {@link DoubleFeature}
     */
    public static Feature<?> booleanFeature(Boolean value, String name, double weight, boolean isTarget) {
        return new BooleanFeature(value, name, weight, isTarget);
    }


    @Data
    private static class CategoricalBoolean implements CategoricalFeature<Boolean> {
        private final Boolean value;
        private final String name;

        @Override
        public Boolean originalValue() {
            return value;
        }

        @Override
        public String featureName() {
            return name;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static class DoubleFeature extends AbstractNumericalFeature<Double> {


        public DoubleFeature(Double value, String featureName, double weight, boolean isTarget) {
            super(value, featureName, weight, isTarget);
        }

        @Override
        protected Double toDouble() {
            return originalValue();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static class IntegerFeature extends AbstractNumericalFeature<Integer> {


        public IntegerFeature(Integer value, String featureName, double weight) {
            super(value, featureName, weight, false);
        }

        public IntegerFeature(Integer value, String name, double weight, boolean isTarget) {
            super(value, name, weight, isTarget);
        }

        @Override
        protected Double toDouble() {
            return originalValue().doubleValue();
        }
    }

    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    private static class BooleanFeature implements Feature<Boolean> {


        private final Boolean value;
        private final String featureName;
        private final double weight;
        private final boolean isTarget;

        public BooleanFeature(Boolean value, String featureName, double weight, boolean isTarget) {
            // super(value, featureName, weight);
            this.value = value;
            this.featureName = featureName;
            this.weight = weight;
            this.isTarget = isTarget;
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

        @Override
        public boolean isTarget() {
            return isTarget;
        }
    }
}
