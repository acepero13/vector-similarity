package com.acepero13.research.profilesimilarity.core.proxy;

import com.acepero13.research.profilesimilarity.api.features.Feature;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class FeaturesHelper {
    private FeaturesHelper() {


    }

    public static List<Feature<?>> features(Object[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected one argument" + Arrays.toString(args));
        }
        List<Feature<?>> feats = (List<Feature<?>>) Optional.ofNullable(args[0])
                .filter(List.class::isInstance)
                .map(List.class::cast)
                .filter(l -> l.stream().allMatch(f -> f instanceof Feature))
                .orElseThrow()
                .stream()
                .map(Feature.class::cast)
                .collect(Collectors.toList());
        return feats;
    }
}
