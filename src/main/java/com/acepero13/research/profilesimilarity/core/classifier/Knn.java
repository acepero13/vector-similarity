package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.CategoricalLabel;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.WithCategoricalLabel;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;
import lombok.extern.java.Log;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Log
public class Knn {

    private final int k;
    private final DataSet dataSet;


    public Knn(int k, WithCategoricalLabel... data) {
        this.k = k;
        this.dataSet = new DataSet(Vector::distanceTo, requireNonNull(data));
    }


    public <U> CategoricalLabel<U> classify(WithCategoricalLabel target) {
        requireNonNull(target);
        log.info(String.format("Classifying using Categorical KNN with k=%d.", k));

        if (CalculationUtils.isEvenNumber(k)) {
            log.warning("K: {} is an even number. Consider changing it to an odd number to help the voting process");
        }

        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<WithCategoricalLabel> results = dataSet.scaleAndScore(target, normalizer)
                .sorted(ascendingScore())
                .limit(k)
                .map(DataSet.Score::sample)
                .filter(WithCategoricalLabel.class::isInstance)
                .map(WithCategoricalLabel.class::cast)
                .collect(Collectors.toList());

        return vote(results);
    }

    private static Comparator<DataSet.Score> ascendingScore() {
        return Comparator.comparingDouble(DataSet.Score::score);
    }

    @SuppressWarnings("unchecked")
    private static <U> CategoricalLabel<U> vote(List<WithCategoricalLabel> result) {


        Map<CategoricalLabel<?>, List<WithCategoricalLabel>> groups = groupResultsByCategory(result);

        return (CategoricalLabel<U>) groups.entrySet().stream()
                .sorted(descendingCount())
                .map(Map.Entry::getKey)
                .findFirst().orElseThrow();


    }

    private static Comparator<Map.Entry<CategoricalLabel<?>, List<WithCategoricalLabel>>> descendingCount() {
        return (o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size());
    }

    private static Map<CategoricalLabel<?>, List<WithCategoricalLabel>> groupResultsByCategory(List<WithCategoricalLabel> result) {
        Map<CategoricalLabel<?>, List<WithCategoricalLabel>> groups = new HashMap<>();


        for (WithCategoricalLabel sample : result) {
            CategoricalLabel<?> label = sample.label();
            List<WithCategoricalLabel> value = groups.getOrDefault(label, new ArrayList<>());
            value.add(sample);
            groups.put(label, value);
        }
        return groups;
    }
}
