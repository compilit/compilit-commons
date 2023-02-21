package com.compilit.validation.api;

import java.util.Collection;

/**
 * Part if the Fluent API to create a "validation", which is used to validate an object against certain predefined Rules
 *
 * @param <T> The type which is under validation
 */
public interface ValidationBuilder<T> {

  /**
   * @param rule the rule which the value needs to comply with.
   * @return the Validatable to add more rules.
   */
  ContinuingValidationBuilder<T> compliesWith(Rule<T> rule);

  /**
   * @param rules the rules which the value needs to comply with.
   * @return the Validatable to add more rules.
   */
  ContinuingValidationBuilder<T> compliesWith(Collection<Rule<T>> rules);

  /**
   * @param rule the rule which the value needs to comply with.
   * @return the Validatable to add more rules.
   */
  ArgumentAdder<T> compliesWith(Rule.WithDualInput<T> rule);

}
