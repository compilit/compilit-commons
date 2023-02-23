package com.compilit.cryptography.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation flags an argument as ready to decrypt. This can only happen when the containing method itself is
 * annotated with Decryptable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER})
public @interface Decrypt {
}
