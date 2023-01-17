package com.compilit.validation.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import testutil.AbstractTestWithContext;

class VoidReturningRuleValidationBuilderTests extends AbstractTestWithContext {

  private static final String FAIL_MESSAGE = "failure";

  @BeforeEach
  public void reset() {
    super.reset();
  }

  @Test
  void orElseThrow_valid_shouldReturnVoid() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, FAIL_MESSAGE), "test");
    var validator = new VoidRuleValidatorBuilder<>(subject, super::interact);
    Assertions.assertThat(validator.orElseThrow(RuntimeException::new)).isNull();
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }

  @Test
  void orElseThrow_invalid_shouldReturnVoid() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, FAIL_MESSAGE), "test");
    var validator = new VoidRuleValidatorBuilder<>(subject, super::interact);
    Assertions.assertThatThrownBy(() -> validator.orElseThrow(RuntimeException::new))
              .isInstanceOf(RuntimeException.class);
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }

  @Test
  void orElseLogMessage_validInput_shouldNotLog() {
    var logger = Mockito.mock(Logger.class);
    var subject = new Subject<>(new RuleDefinition<>(x -> true, FAIL_MESSAGE), "test");
    var validator = new VoidRuleValidatorBuilder<>(subject, super::interact);
    validator.orElseLogMessage(logger);
    Mockito.verifyNoInteractions(logger);
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }

  @Test
  void orElseLogMessage_invalidInput_shouldLog() {
    var logger = Mockito.mock(Logger.class);
    var subject = new Subject<>(new RuleDefinition<>(x -> false, FAIL_MESSAGE), "test");
    var validator = new VoidRuleValidatorBuilder<>(subject, super::interact);
    validator.orElseLogMessage(logger);
    Mockito.verify(logger).error(Mockito.anyString());
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }
}