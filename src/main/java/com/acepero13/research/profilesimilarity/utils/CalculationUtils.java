package com.acepero13.research.profilesimilarity.utils;

/**
 * The CalculationUtils class provides utility methods for mathematical calculations.
 */
public class CalculationUtils {

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private CalculationUtils() {
    }

    /**
     * Calculates the sigmoid of a given number.
     *
     * @param x the input number
     * @return the sigmoid of the input number
     */
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * Determines whether a given integer is an even number.
     *
     * @param value the integer value to check
     * @return true if the value is even, false otherwise
     */
    public static boolean isEvenNumber(int value) {
        return value % 2 == 0;
    }
}