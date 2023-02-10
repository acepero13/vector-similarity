package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedMetric;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;


public class MostSimilar  {

    private final DataSet dataSet;

    public MostSimilar(Vectorizable... vectorizables) {
        this.dataSet = new DataSet(new CombinedMetric(), requireNonNull(vectorizables));

    }
    public Vectorizable mostSimilarTo(Vectorizable target) {
        requireNonNull(target);
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);

        return dataSet.scaleAndScore(target, normalizer)
                .max(Comparator.comparingDouble(DataSet.Score::score))
                .map(DataSet.Score::sample)
                .orElseThrow();
    }

}
