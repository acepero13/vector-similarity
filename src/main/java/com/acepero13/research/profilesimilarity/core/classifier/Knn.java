package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vector;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.classifier.result.KnnResult;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.utils.CalculationUtils;
import com.acepero13.research.profilesimilarity.utils.Tuple;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Log
public class Knn {

    private final int k;
    private final DataSet dataSet;
    private Normalizer normalizer;
    private List<Tuple<Vectorizable, NormalizedVector>> normalizedDataSet = new ArrayList<>();


    private Knn(int k, List<Vectorizable> data) {
        this.k = k;
        this.dataSet = new DataSet(requireNonNull(data));
    }

    private Knn(int k, Normalizer normalizer, List<Vectorizable> data) {
        this.k = k;
        this.dataSet = new DataSet(requireNonNull(data));
        this.normalizer = normalizer;
    }

    public static Knn of(int k, Normalizer normalizer, List<Vectorizable> data) {
        return new Knn(k, normalizer, data);
    }

    public static Knn withDefaultNormalizer(int k, List<Vectorizable> data) {
        return new Knn(k, data);
    }

    public static Knn withDefaultNormalizer(int k, Vectorizable... data) {
        return new Knn(k, List.of(data));
    }


    private static Comparator<DataSet.Score> ascendingScore() {
        return Comparator.comparingDouble(DataSet.Score::score);
    }

    public KnnResult fit(Vectorizable target) {
        requireNonNull(target, "Target cannot be null");
        logInitialInformation();
        NormalizedVector normalizedTarget = normalize(target);
        return classify(normalizedTarget);
    }

    private void logInitialInformation() {
        log.info(String.format("Classifying using Categorical KNN with k=%d.", k));
        log.info("Number of samples: " + dataSet.size());
        if (CalculationUtils.isEvenNumber(k)) {
            log.warning("K: {} is an even number. Consider changing it to an odd number to help the voting process");
        }
    }

    private KnnResult classify(NormalizedVector normalizedTarget) {


        List<FeatureVector> results = normalizedDataSet.stream()
                .map(t -> t.mapSecond(v -> DataSet.calculateScore(Vector::distanceTo, normalizedTarget, v)))
                .map(t -> new DataSet.Score(t.second(), t.first()))
                .sorted(ascendingScore())
                .limit(k)
                .map(DataSet.Score::sample)
                .map(Vectorizable::toFeatureVector)
                .collect(Collectors.toList());

        return KnnResult.of(results);
    }

    private NormalizedVector normalize(Vectorizable target) {
        if (normalizer == null) {
            this.normalizer = DataSet.minMaxNormalizer(target, dataSet);
        }
        if (normalizedDataSet.isEmpty()) {
            normalizedDataSet = dataSet.scaleAndScore(target, normalizer);
        }
        return NormalizedVector.of(target.vector(target.numericalFeatures()), normalizer);
    }

}
