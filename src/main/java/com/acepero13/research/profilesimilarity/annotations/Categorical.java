package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE})
// TODO: Add list of possible values
public @interface Categorical {
    String name() default "";

    Class<?> type() default Object.class;

    boolean oneHotEncoding() default false;
}
