package com.compilit.core.api.annotations;

import jakarta.json.JsonPatch.Operation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to any field in a class that implements the PatchableEntity interface. This will allow you to
 * define exactly which fields can be patched and with what operation s this can be done
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Patchable {

  /**
   * Specify which operations are allowed on this particular field
   *
   * @return an array of the allowed operations for this particular field
   */
  Operation[] allow();
}
