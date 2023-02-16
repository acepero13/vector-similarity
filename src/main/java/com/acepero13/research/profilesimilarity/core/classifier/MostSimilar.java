package com.acepero13.research.profilesimilarity.core.classifier;

import com.acepero13.research.profilesimilarity.api.Metric;
import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.core.vectors.NormalizedVector;
import com.acepero13.research.profilesimilarity.scores.CombinedMetric;
import com.acepero13.research.profilesimilarity.scores.CosineMetric;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;


public class MostSimilar {

    private final DataSet dataSet;
    private final Metric metric = new CosineMetric();
    private List<Tuple<Vectorizable, NormalizedVector>> normalizedDataSet = new ArrayList<>();

    public MostSimilar(Vectorizable... vectorizables) {
        this.dataSet = new DataSet(requireNonNull(vectorizables));

    }

    public MostSimilar(List<Vectorizable> vectorizables) {
        this.dataSet = new DataSet(requireNonNull(vectorizables));

    }

    public Vectorizable mostSimilarTo(Vectorizable target) {
        requireNonNull(target);
        Normalizer normalizer = DataSet.minMaxNormalizer(target, dataSet);

        if (normalizedDataSet.isEmpty()) {
            normalizedDataSet = dataSet.scaleAndScore(target, normalizer);
        }

        NormalizedVector normalizedTarget = NormalizedVector.of(target.vector(target.numericalFeatures()), normalizer);

        List<DataSet.Score> l = new ArrayList<>();
        for (var v: normalizedDataSet) {
            var score = DataSet.calculateScore(metric, normalizedTarget, v.second());
            l.add(new DataSet.Score(score, v.first()));
        }

        l.sort(Comparator.comparingDouble(DataSet.Score::score));
        var result = l.stream().max(Comparator.comparingDouble(DataSet.Score::score));

        return normalizedDataSet.stream()
                .map(t -> t.mapSecond(v -> DataSet.calculateScore(metric, normalizedTarget, v)))
                .map(t -> new DataSet.Score(t.second(), t.first()))
                .max(Comparator.comparingDouble(DataSet.Score::score))
                .map(DataSet.Score::sample)
                .orElseThrow();
    }


}
