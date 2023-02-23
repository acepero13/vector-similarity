package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.CategoricalLabel;
import lombok.EqualsAndHashCode;

import java.util.Objects;


/**
 * A default implementation of the CategoricalLabel interface.
 *
 * @param <T> the type of the categorical label.
 */
@EqualsAndHashCode
public class DefaultCategoricalLabel<T> implements CategoricalLabel<T> {

    /**
     * The value of the categorical label.
     */
    private final T value;

    /**
     * Constructs a DefaultCategoricalLabel object with the given value.
     *
     * @param value the value of the categorical label.
     * @throws NullPointerException if the given value is null.
     */
    public DefaultCategoricalLabel(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns the value of the categorical label.
     *
     * @return the value of the categorical label.
     */
    @Override
    public T value() {
        return value;
    }
}
