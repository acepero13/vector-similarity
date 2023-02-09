package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.*;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.CategoricalLabel;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.*;
import java.util.stream.Collectors;


public class Knn {

    private final int k;
    private final DataSet dataSet;


    public Knn(int k, WithCategoricalLabel... data) {
        this.k = k;
        // TODO: Log even value of k as warning
        this.dataSet = new DataSet(Vector::distanceTo, data);
    }



    public <U> CategoricalLabel<U> classify(WithCategoricalLabel target) {
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<WithCategoricalLabel> results = dataSet.loadDataUsing(target, normalizer)
                                            .sorted(Comparator.comparingDouble(Tuple::second))
                                            .limit(k)
                                            .map(Tuple::first)
                                            .filter(WithCategoricalLabel.class::isInstance)
                                            .map(WithCategoricalLabel.class::cast)
                                            .collect(Collectors.toList());

        return vote(results);
    }

    private static <U> CategoricalLabel<U> vote(List<WithCategoricalLabel> result) {


        Map<CategoricalLabel<?>, List<WithCategoricalLabel>> groups = new HashMap<>();
        for (WithCategoricalLabel sample : result) {
            CategoricalLabel<?> label = sample.label();
            List<WithCategoricalLabel> value = groups.getOrDefault(label, new ArrayList<>());
            value.add(sample);
            groups.put(label, value);
        }

        return (CategoricalLabel<U>) groups.entrySet().stream()
                                           .sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()))
                                           .map(Map.Entry::getKey)
                                           .findFirst().orElseThrow();




    }
}
