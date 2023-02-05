package com.acepero13.research.profilesimilarity.utils;

public class CalculationUtils {
    private CalculationUtils() {
    }

    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
