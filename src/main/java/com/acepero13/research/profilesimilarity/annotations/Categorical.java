package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to mark a categorical feature, indicating that it should be treated as a categorical variable in
 * machine learning models.
 * This annotation can be applied to fields, constructors, and types. When applied to a field, the field's name is used
 * as the categorical feature name. When applied to a constructor, the parameter names are used as the categorical feature
 * names. When applied to a type, all fields and constructors in the type are considered to be categorical features.
 * The annotation has several optional parameters:
 * name: the name of the categorical feature. By default, the name of the field or constructor parameter is used.
 * type: the type of the categorical feature. By default, the type of the annotated element is used.
 * oneHotEncoding: a boolean indicating whether or not the categorical feature should be one-hot encoded. By default,
 * this is false.
 * enumClass: if the categorical feature is an enumeration, the enumClass parameter should be used to specify the
 * enumeration class. By default, DefaultEnum.class is used.
 * values: if the categorical feature is not an enumeration, the values parameter can be used to specify the possible
 * values of the categorical feature. By default, an empty array is used.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.TYPE})
public @interface Categorical {

    /**
     * the name of the categorical feature. By default, the name of the field or constructor parameter is used.
     *
     * @return the name of the feature
     */
    String name() default "";

    /**
     * the type of the categorical feature. By default, the type of the annotated element is used.
     *
     * @return type of categorical feature
     */
    Class<?> type() default Object.class;

    /**
     * a boolean indicating whether or not the categorical feature should be one-hot encoded. By default, this is false.
     *
     * @return true if the feature should be one-hot encoded
     */
    boolean oneHotEncoding() default false;

    /**
     * By default, {@link DefaultEnum} is used.
     *
     * @return the type of the categorical feature
     */
    Class<? extends Enum<?>> enumClass() default DefaultEnum.class;

    /**
     * Possible values that the one hot feature can take. This is optional.
     *
     * @return list of possible values
     */
    String[] values() default {};

}
