package com.acepero13.research.profilesimilarity.utils;

import lombok.Data;


/**
 * The MinMax class represents a range of double values from a minimum value to a maximum value.
 * <p>
 * It is used to represent the minimum and maximum values of a data set and calculate the difference
 * <p>
 * between them.
 */
@Data
public class MinMax {

    /**
     * The minimum value of the range.
     */
    private final double min;
    /**
     * The maximum value of the range.
     */
    private final double max;

    /**
     * Constructs a MinMax object with the given minimum and maximum values.
     * If the maximum and minimum values are the same, the minimum value is set to 0 and the maximum value is set to 1.
     *
     * @param min the minimum value of the range.
     * @param max the maximum value of the range.
     */
    public MinMax(double min, double max) {
        if (max == min) {
            this.min = 0;
            this.max = 1;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Returns the difference between the maximum and minimum values of the range.
     *
     * @return the difference between the maximum and minimum values of the range.
     */
    public double difference() {
        return max - min;
    }

    /**
     * Returns the minimum value of the range.
     *
     * @return the minimum value of the range.
     */
    public double min() {
        return min;
    }

    /**
     * Returns the maximum value of the range.
     *
     * @return the maximum value of the range.
     */
    public double max() {
        return max;
    }
}
