package com.acepero13.research.profilesimilarity.utils;


import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A tuple of 2 elements
 *
 * <p>
 * Ex: ("Hello", "World") is a tuple of two Strings
 * </p>
 *
 * <p>
 * Ex: ("John Doe", 99.99) is a tuple of a String and a Float
 * </p>
 * <p>
 * <p>
 * To create a Tuple we call the static method <b>of</b>. For example:
 * <pre>Tuple.of("Joe Doe", 99.99)</pre>
 * To retrieve the elements we call the <b>first</b> and <b>second</b> method respectively.
 *
 * @param <F> Type of the first parameter
 * @param <S> Type of the second parameter
 */
public interface Tuple<F, S> {
    static <F, S> Tuple<F, S> of(F fist, S second) {
        return new Tuple<>() {
            @Override
            public F first() {
                return fist;
            }

            @Override
            public S second() {
                return second;
            }

            @Override
            public void apply(TupleApplicable<F, S> applier) {
                applier.apply(fist, second);
            }

            @Override
            public boolean filterBoth(Predicate<F> firstPredicate, Predicate<S> secondPredicate) {
                return firstPredicate.test(fist) && secondPredicate.test(second);
            }

            @Override
            public <R> Tuple<F, R> mapSecond(Function<S, R> mapper) {
                return Tuple.of(fist, mapper.apply(second));
            }
        };
    }

    /**
     * The fist element of the tuple
     *
     * @return A projection of element 1 of this Product.
     */
    F first();

    /**
     * The second element of the tuple
     *
     * @return A projection of element 2 of this Product.
     */
    S second();

    /**
     * Invoke the specified function with the two value.
     * Params:
     * consumer
     *
     * @param applier Block to be executed
     */
    void apply(TupleApplicable<F, S> applier);

    boolean filterBoth(Predicate<F> firstPredicate, Predicate<S> secondPredicate);

    <R> Tuple<F, R> mapSecond(Function<S, R> mapper);

    @FunctionalInterface
    interface TupleApplicable<F, S> {
        void apply(F first, S second);
    }

}