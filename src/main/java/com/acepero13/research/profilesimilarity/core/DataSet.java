package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.Vectorizable;
import com.acepero13.research.profilesimilarity.utils.MinMax;
import com.acepero13.research.profilesimilarity.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class DataSet {
    private final List<Vectorizable> vectorizables;

    public DataSet(Vectorizable... vectorizables) {
        this.vectorizables = List.of(vectorizables);
    }

    public List<Vectorizable> normalize() {
        List<Vector> vectors = vectorizables.stream().map(Vectorizable::vector).collect(Collectors.toList());
        List<Vector> columns = getColumVectorsFrom(vectors);
        List<UnaryOperator<Double>> minMaxNormalization = columns.stream().map(Vector::minMax)
                .map(this::minMax)
                .collect(Collectors.toList());
        vectorizables.forEach(v -> v.setNormalizer(minMaxNormalization));

        return new ArrayList<>();
    }

    private UnaryOperator<Double> minMax(MinMax minMax) {
        if (minMax.difference() < 3) {
            return v -> v;
        }
        return v -> (v - minMax.getMin()) / (minMax.getMax() - minMax.getMin());
    }

    private List<Vector> getColumVectorsFrom(List<Vector> vectors) {
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
