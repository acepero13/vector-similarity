package com.acepero13.research.profilesimilarity.utils;


import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple Tuple interface to hold a pair of values of different types.
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

    /**
     * Returns a new Tuple with the specified values.
     *
     * @param first  The first element of the Tuple.
     * @param second The second element of the Tuple.
     * @param <F>    The type of the first element of the Tuple.
     * @param <S>    The type of the second element of the Tuple.
     * @return A new Tuple with the specified values.
     */
    static <F, S> Tuple<F, S> of(F first, S second) {
        return new Tuple<>() {

            /**
             * Get the first element of the Tuple.
             *
             * @return The first element of the Tuple.
             */
            @Override
            public F first() {
                return first;
            }

            /**
             * Get the second element of the Tuple.
             *
             * @return The second element of the Tuple.
             */
            @Override
            public S second() {
                return second;
            }

            /**
             * Apply a given function on the two elements of the Tuple.
             *
             * @param applier The function to apply on the Tuple elements.
             */
            @Override
            public void apply(TupleApplicable<F, S> applier) {
                applier.apply(first, second);
            }

            @Override
            public boolean filterBoth(Predicate<F> firstPredicate, Predicate<S> secondPredicate) {
                return firstPredicate.test(first) && secondPredicate.test(second);
            }

            @Override
            public <R> Tuple<F, R> mapSecond(Function<S, R> mapper) {
                return Tuple.of(first, mapper.apply(second));
            }

            @Override
            public <R> Tuple<R, S> mapFirst(Function<F, R> mapper) {
                return Tuple.of(mapper.apply(first), second);
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

    /**
     * Filter both elements of the Tuple using the specified predicates.
     *
     * @param firstPredicate  The predicate for filtering the first element.
     * @param secondPredicate The predicate for filtering the second element.
     * @return True if both elements pass the filter, false otherwise.
     */
    boolean filterBoth(Predicate<F> firstPredicate, Predicate<S> secondPredicate);

    /**
     * Map the second element of the Tuple to another type.
     *
     * @param mapper The function to map the second element of the Tuple.
     * @param <R>    The type of the resulting Tuple's second element.
     * @return A new Tuple with the same first element and the mapped second element.
     */
    <R> Tuple<F, R> mapSecond(Function<S, R> mapper);

    /**
     * Map the first element of the Tuple to another type.
     *
     * @param mapper The function to map the first element of the Tuple.
     * @param <R>    The type of the resulting Tuple's first element.
     * @return A new Tuple with the mapped first element and the same second element.
     */
    <R> Tuple<R, S> mapFirst(Function<F, R> mapper);

    /**
     * Represents an operation that accepts two input arguments and returns no
     * result. This is the functional interface for applying an operation to the
     * two elements of a tuple.
     *
     * @param <F> the type of the first input to the operation
     * @param <S> the type of the second input to the operation
     */
    @FunctionalInterface
    interface TupleApplicable<F, S> {
        /**
         * Applies this operation to the given arguments.
         *
         * @param first  the first input argument
         * @param second the second input argument
         */
        void apply(F first, S second);
    }

}