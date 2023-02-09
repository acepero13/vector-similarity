package com.acepero13.research.profilesimilarity.api;


import com.acepero13.research.profilesimilarity.core.DefaultCategoricalLabel;

public interface CategoricalLabel<T> {

    static <U> CategoricalLabel<U>defaultLabel(U value) {
        return new DefaultCategoricalLabel<>(value);
    }
    T value();

    default boolean matches(CategoricalLabel<T> another) {
        return value().equals(another);
    }

}
