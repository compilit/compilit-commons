package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;
import java.util.function.Consumer;

public interface ContinuingValidationBuilder<T>
  extends ActionValidationBuilder<T>, ReturningValidationBuilder<T>, LoggingValidator, Validator {

  /**
   * Connect your current predicate to another one.
   *
   * @param rule the next rule you wish to connect to the pipe.
   * @return ContinuingValidationBuilder to continue adding rules.
   */
  ContinuingValidationBuilder<T> and(Rule<T> rule);

  /**
   * Connect your current predicate to another one.
   *
   * @param rule the next rule you wish to connect to the pipe.
   * @return ContinuingValidationBuilder to continue adding rules.
   */
  ContinuingValidationBuilder<T> and(Rule.WithDualInput<T> rule);

  /**
   * Performs an intermediate operation upon the tested value after successful validation.
   *
   * @param consumer the supplier which encapsulated the return type.
   * @return ActionValidationBuilder to state what needs to happen after successful validation.
   */
  ActionValidationBuilder<T> thenApply(Consumer<T> consumer);

}
