package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedMetric;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.Comparator;


public class MostSimilar extends AbstractClassifier {


    public MostSimilar(Vectorizable... vectorizables) {
        super(new CombinedMetric(), vectorizables);

    }

    @Override
    public Vectorizable mostSimilarTo(Vectorizable target) {
        Tuple<Vectorizable, Double> mostSimilar = loadDataUsing(target)
                .max(Comparator.comparingDouble(Tuple::second))
                .orElseThrow();

        return mostSimilar.first();
    }


    @Override
    protected Normalizer getNormalizer(Vectorizable target) {
        return AbstractClassifier.minMaxNormalizer(target, dataPoints());

    }


}
