package com.acepero13.research.profilesimilarity.api;


import com.acepero13.research.profilesimilarity.core.DefaultCategoricalLabel;

/**
 * An interface representing a categorical label.
 *
 * @param <T> the type of the categorical value.
 */
public interface CategoricalLabel<T> {

    /**
     * Returns a new instance of {@link DefaultCategoricalLabel} initialized with the given value.
     *
     * @param value the value of the default categorical label.
     * @param <U>   the type of the categorical value.
     * @return a new instance of {@link DefaultCategoricalLabel} initialized with the given value.
     */
    static <U> CategoricalLabel<U> defaultLabel(U value) {
        return new DefaultCategoricalLabel<>(value);
    }

    /**
     * Returns the categorical value of the label.
     *
     * @return the categorical value of the label.
     */
    T value();
}
