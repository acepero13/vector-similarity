package com.acepero13.research.profilesimilarity.api.features;

/**
 * This interface represents a categorical feature that extends the Feature interface.
 * <p>
 * It allows comparison of categorical features and overrides the featureValue() method
 * <p>
 * to indicate that categorical features cannot be converted to double.
 *
 * @param <T> the type of the categorical feature
 */
public interface CategoricalFeature<T> extends Feature<T> {

    /**
     * Overrides the featureValue() method of the Feature interface to indicate that
     * categorical features cannot be converted to double.
     *
     * @throws UnsupportedOperationException if the method is called.
     */
    @Override
    default double featureValue() {
        throw new UnsupportedOperationException("Categorical feature cannot be converted to double");
    }

    /**
     * Compares the current CategoricalFeature object with the provided CategoricalFeature
     * object to check if they are equal.
     *
     * @param feature the CategoricalFeature object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    default boolean matches(CategoricalFeature<?> feature) {
        return this.equals(feature);
    }
}
