package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE})
public @interface Categorical {
    String name() default "";

    boolean oneHotEncoding() default false;
}
