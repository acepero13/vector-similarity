package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;

public class JaccardSimilarity implements Similarity {
    @Override
    public Double similarityScore(Vectorizable vectorizable, Vectorizable another) {
        var vector = vectorizable.vector();
        var anotherVector = another.vector();
        int a = numberOfAttributesEqualToOne(vector, anotherVector);
        int b = numberOfAttributesEqualToZeroForFirstAndOneForTheOther(vector, anotherVector);
        int c = numberOfAttributesEqualToOneForFirstAndAndZeroForTheOther(vector, anotherVector);


        int denominator = a + b + c;
        if (denominator == 0) {
            return 0.0;
        }
        return (double) a / denominator;
    }

    private int numberOfAttributesEqualToZero(Vector vector, Vector anotherVector) {
        return (int) vector.zip(anotherVector)
                .filter(t -> t.filterBoth(f -> f.equals(0.0), s -> s.equals(0.0)))
                .count();
    }

    private int numberOfAttributesEqualToOneForFirstAndAndZeroForTheOther(Vector vector, Vector anotherVector) {
        return (int) vector.zip(anotherVector)
                .filter(t -> t.filterBoth(f -> f.equals(1.0), s -> s.equals(0.0)))
                .count();
    }

    private int numberOfAttributesEqualToZeroForFirstAndOneForTheOther(Vector vector1, Vector anotherVector) {
        return (int) vector1.zip(anotherVector)
                .filter(t -> t.filterBoth(f -> f.equals(0.0), s -> s.equals(1.0)))
                .count();
    }

    private int numberOfAttributesEqualToOne(Vector vector, Vector another) {
        return (int) vector.zip(another)
                .filter(t -> t.first().equals(t.second()))
                .filter(t -> t.first().equals(1.0))
                .count();
    }
}
