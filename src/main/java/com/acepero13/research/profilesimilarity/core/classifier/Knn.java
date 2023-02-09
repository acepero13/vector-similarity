package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Feature;
import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Knn extends AbstractClassifier {

    private final int k;
    private final Feature<?> targetFeature;

    protected Knn(Metric metric, Feature<?> targetFeature, int K, Vectorizable... vectorizables) {
        super(metric, vectorizables);
        this.k = K;
        this.targetFeature = targetFeature;
    }

    @Override
    protected Normalizer getNormalizer(Vectorizable target) {
        return AbstractClassifier.minMaxNormalizer(target, dataPoints());
    }

    // TODO: Return a result that contains also the confidence
    // TODO: We are only interested in a label
    @Override
    public Vectorizable mostSimilarTo(Vectorizable target) {
        List<Vectorizable> result = loadDataUsing(target)
                .sorted(Comparator.comparingDouble(Tuple::second))
                .limit(k)
                .map(Tuple::first)
                .collect(Collectors.toList());

        Map<Double, List<Vectorizable>> groups = new TreeMap<>(Collections.reverseOrder());
        for (Vectorizable sample : result) {
            double feat = sample.targetFeature().featureValue();
            List<Vectorizable> value = groups.getOrDefault(feat, new ArrayList<>());
            value.add(sample);
            groups.put(feat, value);
        }


        return groups.entrySet().stream()
                .flatMap(e -> e.getValue().stream())
                .findFirst().orElseThrow();
    }
}
