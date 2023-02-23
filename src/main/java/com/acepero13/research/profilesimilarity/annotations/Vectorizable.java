package com.acepero13.research.profilesimilarity.annotations;

import java.lang.annotation.*;

/**
 * Annotation to indicate that a class can be vectorized.
 * This annotation can be used to mark a class whose instances can be represented as a numerical feature vector. It is
 * intended to be used by machine learning frameworks to automatically convert objects of vectorizable classes into
 * numerical feature vectors.
 * The annotation can only be applied to classes.
 *
 * @since 2.0.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Vectorizable {
}
