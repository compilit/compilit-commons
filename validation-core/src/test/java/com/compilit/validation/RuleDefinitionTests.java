package com.compilit.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RuleDefinitionTests {

  @Test
  void getMessage_shouldReturnProvidedMessage() {
    var message = "failure";
    var ruleDefinition = new RuleDefinition<String>(x -> true, message);
    Assertions.assertThat(ruleDefinition.getMessage()).isEqualTo(message);
  }

  @Test
  void test_valid_shouldReturnTrue() {
    var message = "failure";
    var ruleDefinition = new RuleDefinition<String>(x -> true, message);
    Assertions.assertThat(ruleDefinition.test("ignored")).isTrue();
  }

  @Test
  void test_invalid_shouldReturnFalse() {
    var message = "failure";
    var ruleDefinition = new RuleDefinition<String>(x -> false, message);
    Assertions.assertThat(ruleDefinition.test("ignored")).isFalse();
  }
}