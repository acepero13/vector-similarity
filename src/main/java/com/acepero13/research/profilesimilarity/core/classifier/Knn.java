package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.Vector;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;


public class Knn {

    private final int k;
    private final DataSet dataSet;

    protected Knn(Metric metric, int K, Vectorizable... vectorizables) {
        this.k = K;
        this.dataSet = new DataSet(metric, vectorizables);
    }

    public Knn(int K, Vectorizable... data) {
        this.k = K;
        this.dataSet = new DataSet(Vector::distanceTo, data);
    }


    // TODO: We are only interested in a label

    public Vectorizable mostSimilarTo(Vectorizable target) {
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<Vectorizable> result = dataSet.loadDataUsing(target, normalizer)
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
