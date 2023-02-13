package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Log
public class Knn {

    private final int k;
    private final DataSet dataSet;


    public Knn(int k, Vectorizable... data) {
        this(k, List.of(data));
    }

    public Knn(int k, List<Vectorizable> data) {
        this.k = k;
        this.dataSet = new DataSet(Vector::distanceTo, requireNonNull(data));
    }


    private static Comparator<DataSet.Score> ascendingScore() {
        return Comparator.comparingDouble(DataSet.Score::score);
    }

    public KnnResult fit(Vectorizable target) {
        requireNonNull(target);
        log.info(String.format("Classifying using Categorical KNN with k=%d.", k));

        if (CalculationUtils.isEvenNumber(k)) {
            log.warning("K: {} is an even number. Consider changing it to an odd number to help the voting process");
        }
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);
        List<FeatureVector> results = dataSet.scaleAndScore(target, normalizer)
                .sorted(ascendingScore())
                .limit(k)
                .map(DataSet.Score::sample)
                .map(Vectorizable::toFeatureVector)
                .collect(Collectors.toList());

        return KnnResult.of(results);
    }
}
