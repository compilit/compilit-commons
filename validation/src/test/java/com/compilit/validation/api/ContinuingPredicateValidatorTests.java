package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.compilit.validation.api.Subject.DEFAULT_MESSAGE;

class ContinuingPredicateValidatorTests {

  @Test
  void getMessage_noValidation_shouldReturnDefaultMessage() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.getMessage()).isEqualTo(DEFAULT_MESSAGE);
  }

  @Test
  void getMessage_valid_shouldReturnDefaultMessage() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    predicate.validate();
    Assertions.assertThat(predicate.getMessage()).isEqualTo(DEFAULT_MESSAGE);
  }

  @Test
  void getMessage_invalid_shouldReturnFailMessage() {
    var message = "failure";
    var subject = new Subject<>(new RuleDefinition<>(x -> false, message), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    predicate.validate();
    Assertions.assertThat(predicate.getMessage()).contains(message);
  }

  @Test
  void and_shouldAddRule() {
    var message1 = "failure1";
    var message2 = "failure2";
    var rule2 = new RuleDefinition<String>(x -> false, message2);
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    predicate.and(rule2).validate();
    Assertions.assertThat(predicate.getMessage()).doesNotContain(message1);
    Assertions.assertThat(predicate.getMessage()).contains(message2);
  }

  @Test
  void validate_shouldValidateRules() {
    var message1 = "failure1";
    var message2 = "failure2";
    var rule2 = new RuleDefinition<String>(x -> false, message2);
    var subject = new Subject<>(new RuleDefinition<>(x -> true, message1), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    predicate.and(rule2);
    predicate.validate();
    Assertions.assertThat(predicate.getMessage()).doesNotContain(message1);
    Assertions.assertThat(predicate.getMessage()).contains(message2);
  }

  @Test
  void andThen_invalid_shouldReturnFalse() {
    var value = "test";
    var rules = new ArrayList<Rule<String>>();
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.andThen(() -> false).orElseReturn(true)).isTrue();
  }

  @Test
  void orElseThrow_valid_shouldReturnTrue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.andThen(() -> true).orElseThrow(RuntimeException::new)).isTrue();
  }

  @Test
  void orElseThrow_invalid_shouldThrowException() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "failure"), "test");
    var validation = new ContinuingRuleValidationBuilder<>(subject).andThen(() -> false);
    Assertions.assertThatThrownBy(() -> validation.orElseThrow(RuntimeException::new)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void orElseReturn_valid_shouldReturnOriginalValue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    Assertions.assertThat(predicate.andThen(() -> true).orElseReturn(x -> false)).isTrue();
  }

  @Test
  void orElseReturn_invalid_shouldReturnOtherValue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    predicate.andThen(() -> false).orElseReturn(x -> false);
    Assertions.assertThat(predicate.validate()).isFalse();
  }

  @Test
  void orElseReturn_invalidValidation_shouldReturnOther() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    predicate.andThen(() -> true).orElseReturn(false);
    Assertions.assertThat(predicate.validate()).isFalse();
  }

  @Test
  void orElseReturn_validValidation_shouldReturnOriginal() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "failure"), "test");
    var predicate = new ContinuingRuleValidationBuilder<>(subject);
    predicate.andThen(() -> true).orElseReturn(false);
    Assertions.assertThat(predicate.validate()).isTrue();
  }
}
