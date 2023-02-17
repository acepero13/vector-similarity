package com.acepero13.research.profilesimilarity.scores;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
public class StrictDiceSimilarity implements Metric {
    @Override
    public Double similarityScore(NormalizedVector vectorizable, NormalizedVector another) {
        long similar = vectorizable.zip(another)
                                   .filter(t -> t.first().equals(t.second()))
                                   .filter(t -> t.first().equals(1.0))
                                   .count();
        return similar / (double) vectorizable.size();
    }
}
