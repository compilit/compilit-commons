package com.compilit.validation.api;

import java.util.Collection;

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
  ArgumentAppender<T> compliesWith(Rule.WithDualInput<T> rule);

}
