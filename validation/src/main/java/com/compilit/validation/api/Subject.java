package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

class Subject<T> implements Validator {

  static final String DEFAULT_MESSAGE = "Nothing to report";
  static final String BROKEN_RULE_PREFIX = "Broken rule: ";

  private final T value;
  private final boolean hasDualInput;

  private final List<Rule<T>> ruleDefinitions = new ArrayList<>();
  private final List<Rule.WithDualInput<T>> dualInputRuleDefinitions = new ArrayList<>();
  private final List<Consumer<T>> intermediateActions = new ArrayList<>();

  private Object argument;
  private String message = DEFAULT_MESSAGE;

  Subject(Rule<T> ruleDefinition, final T value) {
    this.ruleDefinitions.add(ruleDefinition);
    this.value = value;
    this.argument = null;
    this.hasDualInput = false;
  }

  Subject(final Rule.WithDualInput<T> dualInputRuleDefinitions, final T value) {
    this.dualInputRuleDefinitions.add(dualInputRuleDefinitions);
    this.value = value;
    this.hasDualInput = true;
  }

  Subject(final Collection<Rule<T>> ruleDefinitions, final T value) {
    this.ruleDefinitions.addAll(ruleDefinitions);
    this.value = value;
    this.hasDualInput = false;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean validate() {
    final var stringBuilder = new StringBuilder();
    var isValid = true;
    if (hasDualInput) {
      isValid = validateDualInputRules(stringBuilder);
    } else {
      isValid = validateRules(stringBuilder);
    }
    if (!isValid) {
      message = stringBuilder.toString();
    }
    return isValid;
  }

  public T getValue() {
    return value;
  }

  public void addRule(Rule<T> rule) {
    ruleDefinitions.add(rule);
  }

  public void addDualInputRule(Rule.WithDualInput<T> rule) {
    dualInputRuleDefinitions.add(rule);
  }

  public void addIntermediateAction(Consumer<T> consumer) {
    intermediateActions.add(consumer);
  }

  public void setArgument(Object argument) {
    if (this.argument == null) {
      this.argument = argument;
    }
  }

  public void processIntermediateActions() {
    intermediateActions.forEach(x -> x.accept(getValue()));
  }

  private boolean validateRules(StringBuilder stringBuilder) {
    var isValid = true;
    for (final var ruleDefinition : ruleDefinitions) {
      if (!ruleDefinition.test(value)) {
        stringBuilder.append(BROKEN_RULE_PREFIX).append(ruleDefinition.getMessage()).append("\n");
        isValid = false;
      }
    }
    return isValid;
  }

  private boolean validateDualInputRules(StringBuilder stringBuilder) {
    var isValid = true;
    for (final var biRuleDefinition : dualInputRuleDefinitions) {
      if (!biRuleDefinition.test(value, argument)) {
        stringBuilder.append(BROKEN_RULE_PREFIX)
                .append(biRuleDefinition.getMessage())
                .append("\n");
        isValid = false;
      }
    }
    return isValid;
  }
}
