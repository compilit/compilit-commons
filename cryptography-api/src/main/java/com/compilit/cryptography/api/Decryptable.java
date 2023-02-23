package com.compilit.cryptography.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any method annotated with this annotation is flagged for containing optional arguments which can be decrypted. These
 * arguments should be annotated with the Decrypt annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Decryptable {
}
