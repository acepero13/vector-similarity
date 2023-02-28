package com.acepero13.research.profilesimilarity.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

    /**
     * Rounds the given double value to two decimal places.
     *
     * @param value the double value to round
     * @return the rounded value as a double
     */
    public static double roundedTwoDecimals(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        String resultStr = df.format(value);
        return Double.parseDouble(resultStr);
    }
}
