package com.compilit.validation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class ReturningRuleValidationBuilderTests {

  private static final String VALUE = "value";
  private static final String OTHER = "other";
  private static final String FAIL_MESSAGE = "failure";

  private final Supplier<String> supplier = () -> VALUE;
  private final Consumer<String> consumer = Object::notifyAll;
  private final Function<String, String> function = x -> VALUE;

  @Test
  void orElseThrow_validInput_shouldNotThrowExceptionAndReturnTrue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThat(validator.orElseThrow(RuntimeException::new)).isEqualTo(VALUE);
  }

  @Test
  void orElseThrow_invalidInput_shouldThrowGivenAException() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThatThrownBy(() -> validator.orElseThrow(RuntimeException::new))
              .isInstanceOf(RuntimeException.class);
  }


  @Test
  void orElseReturnValue_validInput_shouldReturnValue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThat(validator.orElseReturn(OTHER)).isEqualTo(VALUE);
  }

  @Test
  void orElseReturnValue_invalidInput_shouldReturnOtherValue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThat(validator.orElseReturn(OTHER)).isEqualTo(OTHER);
  }

  @Test
  void orElseReturnFunction_validInput_shouldReturnValue() {
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThat(validator.orElseReturn(x -> OTHER)).isEqualTo(VALUE);
  }

  @Test
  void orElseReturnFunction_invalidInput_shouldReturnFunction() {
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    Assertions.assertThat(validator.orElseReturn(x -> OTHER)).isEqualTo(OTHER);
  }

  @Test
  void orElseLogMessage_validInput_shouldNotLog() {
    var logger = Mockito.mock(Logger.class);
    var subject = new Subject<>(new RuleDefinition<>(x -> true, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    validator.orElseLogMessage(logger);
    Mockito.verifyNoInteractions(logger);
  }

  @Test
  void orElseLogMessage_invalidInput_shouldLog() {
    var logger = Mockito.mock(Logger.class);
    var subject = new Subject<>(new RuleDefinition<>(x -> false, "fail"), VALUE);
    var validator = new ReturningRuleValidationBuilder<>(subject, () -> VALUE);
    validator.orElseLogMessage(logger);
    Mockito.verify(logger).error(Mockito.anyString());
  }
}