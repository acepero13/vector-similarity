package com.acepero13.research.profilesimilarity.core;

import com.acepero13.research.profilesimilarity.api.CategoricalLabel;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * A Description
 *
 * @author Alvaro Cepero
 */
@EqualsAndHashCode
public class DefaultCategoricalLabel<T> implements CategoricalLabel<T> {
    private final T value;

    public DefaultCategoricalLabel(T value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public T value() {
        return value;
    }
}
