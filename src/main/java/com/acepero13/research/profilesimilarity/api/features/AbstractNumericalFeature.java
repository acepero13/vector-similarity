package com.acepero13.research.profilesimilarity.api.features;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * An abstract class that implements the NumericalFeature interface and extends the Number class.
 * <p>
 * NumericalFeature is an interface that provides common methods for numerical features.
 * <p>
 * This class represents a numerical feature with a value, a name and a weight.
 * <p>
 * The value of the feature is of type T which extends Number.
 *
 * @param <T> The type of the numerical value
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractNumericalFeature<T extends Number> extends Number implements NumericalFeature<T> {

    /**
     * The value of the numerical feature.
     */
    private final T value;
    /**
     * The name of the numerical feature.
     */
    private final String name;

    /**
     * The weight of the numerical feature.
     */
    private final double weight;
    private final boolean isTarget;

    /**
     * Constructs an AbstractNumericalFeature object with the given value, name, and weight.
     *
     * @param value    the value of the numerical feature.
     * @param name     the name of the numerical feature.
     * @param weight   the weight of the numerical feature.
     * @param isTarget
     * @implNote Null values are allowed
     */
    public AbstractNumericalFeature(T value, String name, double weight, boolean isTarget) {
        this.value = value;
        this.name = name;
        this.weight = weight;
        this.isTarget = isTarget;
    }

    /**
     * Returns the weight of the feature.
     *
     * @return the weight of the feature.
     */
    @Override
    public double weight() {
        return weight;
    }

    /**
     * Returns the numerical value of the feature.
     *
     * @return the numerical value of the feature.
     */
    @Override
    public double featureValue() {
        return toDouble();
    }

    /**
     * Returns the value of the specified number as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code double}.
     */
    protected abstract Double toDouble();

    @Override
    public T originalValue() {
        return value;
    }

    @Override
    public String featureName() {
        return name;
    }

    /**
     * Returns the value of the specified number as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code int}.
     */
    @Override
    public int intValue() {
        return toDouble().intValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code long}.
     */
    @Override
    public long longValue() {
        return toDouble().longValue();
    }

    /**
     * Returns the value of the specified number as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code float}.
     */
    @Override
    public float floatValue() {
        return toDouble().floatValue();
    }

    /**
     * Returns the value of the specified number as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion
     * to type {@code double}.
     */
    @Override
    public double doubleValue() {
        return toDouble();
    }

    @Override
    public boolean isTarget() {
        return isTarget;
    }
}
