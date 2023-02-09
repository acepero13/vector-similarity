package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedMetric;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Comparator;


public class MostSimilar  {

    private final DataSet dataSet;

    public MostSimilar(Vectorizable... vectorizables) {
        this.dataSet = new DataSet(new CombinedMetric(), vectorizables);

    }
    public Vectorizable mostSimilarTo(Vectorizable target) {
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        Tuple<Vectorizable, Double> mostSimilar = dataSet.loadDataUsing(target, normalizer)
                .max(Comparator.comparingDouble(Tuple::second))
                .orElseThrow();

        return mostSimilar.first();
    }

}
