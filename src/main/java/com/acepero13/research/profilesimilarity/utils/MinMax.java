package com.acepero13.research.profilesimilarity.utils;

import lombok.Data;

@Data
public class MinMax {
    private final double min;
    private final double max;

    public MinMax(double min, double max) {
        if (max == min) {
            this.min = 0;
            this.max = 1;
        } else {
            this.min = min;
            this.max = max;
        }

    }


    public double difference() {
        return max - min;
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }
}
