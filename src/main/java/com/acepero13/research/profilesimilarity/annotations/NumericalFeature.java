package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface NumericalFeature {

    String name() default "";

    Class<?> type() default Object.class;
    double weight() default 1.0;
}
