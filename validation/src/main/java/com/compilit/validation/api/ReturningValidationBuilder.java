package com.compilit.validation.api;

import java.util.function.Function;

public interface ReturningValidationBuilder<T> extends ThrowingValidator<T>, LoggingValidator {

  /**
   * @param other the backup/default return type if the validation fails.
   * @return T the return type.
   */
  T orElseReturn(T other);

  /**
   * @param other the backup/default return type if the validation fails with the optional message that is contained in
   *              the Validator.
   * @return T the return type.
   */
  T orElseReturn(Function<String, T> other);

}
