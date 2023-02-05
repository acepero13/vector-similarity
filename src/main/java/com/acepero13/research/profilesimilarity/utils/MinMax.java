package com.acepero13.research.profilesimilarity.utils;

import lombok.Data;

@Data
public class MinMax {
    private final double min;
    private final double max;


    public double difference() {
        return max - min;
    }
}
