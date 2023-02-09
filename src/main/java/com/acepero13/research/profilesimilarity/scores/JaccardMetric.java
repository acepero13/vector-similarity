package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.NormalizedVector;

public class JaccardMetric implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vector, NormalizedVector anotherVector) {

        int a = numberOfAttributesEqualToOne(vector, anotherVector);
        int b = numberOfAttributesEqualToZeroForFirstAndOneForTheOther(vector, anotherVector);
        int c = numberOfAttributesEqualToOneForFirstAndAndZeroForTheOther(vector, anotherVector);


        int denominator = a + b + c;
        if (denominator == 0) {
            return 0.0;
        }
        return (double) a / denominator;
    }



    private int numberOfAttributesEqualToOneForFirstAndAndZeroForTheOther(NormalizedVector vector, NormalizedVector anotherVector) {
        return (int) vector.zip(anotherVector)
                .filter(t -> t.filterBoth(f -> f.equals(1.0), s -> s.equals(0.0)))
                .count();
    }

    private int numberOfAttributesEqualToZeroForFirstAndOneForTheOther(NormalizedVector vector1, NormalizedVector anotherVector) {
        return (int) vector1.zip(anotherVector)
                .filter(t -> t.filterBoth(f -> f.equals(0.0), s -> s.equals(1.0)))
                .count();
    }

    private int numberOfAttributesEqualToOne(NormalizedVector vector, NormalizedVector another) {
        return (int) vector.zip(another)
                .filter(t -> t.first().equals(t.second()))
                .filter(t -> t.first().equals(1.0))
                .count();
    }
}
