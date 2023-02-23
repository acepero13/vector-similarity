package com.acepero13.research.profilesimilarity.api;

import com.acepero13.research.profilesimilarity.api.features.CategoricalFeature;
import com.acepero13.research.profilesimilarity.api.features.Feature;
import com.acepero13.research.profilesimilarity.core.vectors.DoubleVector;
import com.acepero13.research.profilesimilarity.core.vectors.FeatureVector;

import java.util.List;
import java.util.stream.Collectors;
/**

 A {@code Vectorizable} represents an object that can be represented as a vector of {@link Double}s.

 It provides methods to obtain the vector representation, list of features, numerical features, and a feature vector.
 */
public interface Vectorizable {
    /**

     Returns the vector representation of this object.
     @return the vector representation of this object as a {@link Vector} of {@link Double}s.
     */
    Vector<Double> vector();

    /**

     Returns a list of features of this object.
     @return a list of {@link Feature}s of this object.
     */
    List<Feature<?>> features();
    /**

     Returns a feature vector representation of this object.
     @return a {@link FeatureVector} representation of this object using its list of features.
     */
    default FeatureVector toFeatureVector() {
        return FeatureVector.of(features());
    }

    /**

     Returns the vector representation of this object for the given white-listed features.
     @param whiteList a {@link List} of white-listed {@link Feature}s.
     @return the vector representation of this object as a {@link Vector} of {@link Double}s
     using only the white-listed numerical features.
     */
    default Vector<Double> vector(List<Feature<?>> whiteList) {
        if (whiteList.isEmpty()) {
            return vector();
        }
        return DoubleVector.ofFeatures(features().stream()
                .filter(f -> f.isWhiteListed(whiteList))
                .filter(f -> !(f instanceof CategoricalFeature))
                .collect(Collectors.toList()));
    }

    /**

     Returns a list of numerical features of this object.
     @return a list of numerical {@link Feature}s of this object.
     */

    default List<Feature<?>> numericalFeatures() {
        return features().stream()
                .filter(f -> !(f instanceof CategoricalFeature))
                .collect(Collectors.toList());
    }
}
