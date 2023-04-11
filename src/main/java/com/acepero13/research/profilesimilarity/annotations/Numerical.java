package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to mark a constructor or field as numerical, specifying additional metadata about the numerical feature.
 * <p>
 * This annotation can be inherited by subclasses, and its retention policy is set to RUNTIME.
 *
 * @since 2.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Numerical {
    /**
     * The name of the numerical feature. By default, this is an empty string.
     *
     * @return The name of the numerical feature.
     */
    String name() default "";

    /**
     * The type of the numerical feature. By default, this is {@code Object.class}.
     * If nothing is specified, it tries to infer the type from the field type
     *
     * @return The type of the numerical feature.
     */
    Class<?> type() default Object.class;

    /**
     * The weight of the numerical feature. By default, this is 1.0.
     *
     * @return The weight of the numerical feature.
     */

    double weight() default 1.0;

     /**
     * a boolean indicating whether or not the categorical feature is the Target feature
     *
     * @return true if the feature should be one-hot encoded
     * @implNote This is only used for clarity. It has no consequences in the calculations
     */

    boolean target() default false;
}
