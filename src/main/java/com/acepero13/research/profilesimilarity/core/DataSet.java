package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Normalizer;
import com.acepero13.research.profilesimilarity.api.Similarity;
import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.scores.CombinedSimilarity;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class DataSet {
    private final List<Vectorizable> vectorizables;
    private final Normalizer normalizer ;
    private final Similarity similarityScorer = new CombinedSimilarity();

    public DataSet(Vectorizable... vectorizables) {
        this.vectorizables = List.of(vectorizables);
        normalizer = MinMaxNormalization.normalizerFor(this.vectorizables);
    }


    public Vectorizable mostSimilarTo(Vectorizable vectorizable) {
        Vector source = normalizer.normalize(vectorizable.vector());
        List<Tuple<Vectorizable, Double>> mostSimilar = this.vectorizables.stream()
                .map(v -> Tuple.of(v, similarityScorer.similarityScore(normalizer.normalize(v.vector()), source)))
                .sorted((f, s) -> Double.compare(s.second(), f.second()))

                .collect(Collectors.toList());

        return mostSimilar.get(0).first();
    }


    private static final class MinMaxNormalization implements Normalizer {


        private final List<UnaryOperator<Double>> mapper;

        public MinMaxNormalization(List<UnaryOperator<Double>> mapper) {
            this.mapper = mapper;
        }

        public static Normalizer normalizerFor(List<Vectorizable> vectorizables) {
            List<Vector> vectors = vectorizables.stream().map(Vectorizable::vector).collect(Collectors.toList());
            List<Vector> columns = getColumVectorsFrom(vectors);
            return new MinMaxNormalization(columns.stream().map(Vector::minMax)
                    .map(MinMaxNormalization::minMax)
                    .collect(Collectors.toList()));

        }

        @Override
        public Vector normalize(Vector vector) {
            return vector.mapEach(mapper);
        }

        private static UnaryOperator<Double> minMax(MinMax minMax) {
            if (minMax.difference() == 0) {
                return v -> v;
            }
            return v -> (v - minMax.getMin()) / minMax.difference();
        }

        private static List<Vector> getColumVectorsFrom(List<Vector> vectors) {
            List<Vector> columnVectors = new ArrayList<>();
            if (vectors.isEmpty()) {
                return columnVectors;
            }
            int totalColumns = vectors.get(0).size();

            for (int column = 0; column < totalColumns; column++) {
                List<Double> columnVector = new ArrayList<>();
                for (Vector vector : vectors) {
                    columnVector.add(vector.getFeature(column));
                }
                columnVectors.add(Vector.of(columnVector));
            }


            return columnVectors;
        }
    }


}
