package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.utils.CalculationUtils;
import lombok.EqualsAndHashCode;

import java.util.Objects;


/**
 * A class representing a probability value, typically used for classification problems.
 */

public class Probability {

    /** The value of the probability. */
    private final double value;

    /**
     * Constructs a new Probability with the given value.
     *
     * @param value the value of the probability
     */
    public Probability(double value) {
        this.value = value;
    }

    /**
     * Returns a new Probability instance with the given value as a percentage.
     * If the value is greater than 100, an IllegalArgumentException is thrown.
     *
     * @param value the value of the probability as a percentage (between 0 and 100)
     * @return a new Probability instance with the given value as a percentage
     * @throws IllegalArgumentException if the value is greater than 100
     */
    public static Probability of(double value) {
        if (value > 100) {
            throw new IllegalArgumentException("Maximum value of the probability is 100, but " + value + " was given");
        }

        return value > 1
                ? new Probability(value / 100.0)
                : new Probability(value);
    }

    /**
     * Returns the value of the probability as a percentage.
     *
     * @return the value of the probability as a percentage (between 0 and 100)
     */
    public double asPercentage() {
        return CalculationUtils.roundedTwoDecimals(value * 100.0);
    }

    /**
     * Returns the value of the probability.
     *
     * @return the value of the probability
     */
    public double value() {
        return value;
    }

    /**
     * Returns a string representation of the Probability, including the value as a percentage.
     *
     * @return a string representation of the Probability
     */
    @Override
    public String toString() {
        return "Probability: " + asPercentage() + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Probability that = (Probability) o;
        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
