package com.acepero13.research.profilesimilarity.core.classifier.result;

import com.acepero13.research.profilesimilarity.utils.CalculationUtils;


public class Probability {
    private final double value;

    public Probability(double value) {
        this.value = value;
    }

    public static Probability of(double value) {
        if (value > 100) {
            throw new IllegalArgumentException("Maximum value of the probability is 100, but " + value + " was given");
        }

        return value > 1
                ? new Probability(value / 100.0)
                : new Probability(value);
    }

    public double asPercentage() {
        return CalculationUtils.roundedTwoDecimals(value * 100.0);
    }

    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return "Probability: " + asPercentage() + "%";
    }
}
