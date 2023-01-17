package com.compilit.validation.api;

import static com.compilit.validation.api.Definitions.defineThatIt;
import static com.compilit.validation.api.Subject.DEFAULT_MESSAGE;
import static com.compilit.validation.api.Verifications.verifyThat;
import static com.compilit.validation.api.Verifications.verifyThatIt;
import static com.compilit.validation.predicates.ObjectPredicate.isA;

import com.compilit.core.api.validation.Rule;
import com.compilit.validation.predicates.StringPredicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.AbstractTestWithContext;
import testutil.TestObject;

class ApiUsageExampleTests extends AbstractTestWithContext {

  private static final String FAIL_MESSAGE = "I am error";
  private final String passwordRuleFailMessage = "Password did not meet our requirements!";
  private final String goodPassword = "#This%Should*BeAGoodPassword(";
  private final String badPassword = "Whatever";
  private final Rule<String> passwordRule = Definitions.defineThatIt(StringPredicate.contains("#", "%", "*", "(")
                                                                                    .and(StringPredicate.hasALengthBetween(
                                                                                      15).and(50)))
                                                       .otherwiseReport(passwordRuleFailMessage);

  @BeforeEach
  public void reset() {
    super.reset();
  }

  @Test
  void validate_goodPassword_shouldReturnTrue() {
    var passwordRuleFailMessage = "Password did not meet our requirements!";

    var passwordRule = Definitions.defineThatIt(StringPredicate.contains("#", "%", "*", "(")
                                                               .and(StringPredicate.hasALengthBetween(15).and(50)))
                                  .otherwiseReport(passwordRuleFailMessage);

    Assertions.assertThat(verifyThat(goodPassword)
                            .compliesWith(passwordRule)
                            .validate())
              .isTrue();
  }

  @Test
  void orElseThrow_goodPassword_shouldNotThrowException() {
    Assertions.assertThatNoException().isThrownBy(
      () -> verifyThat(goodPassword)
        .compliesWith(passwordRule)
        .orElseThrow(RuntimeException::new));
  }

  @Test
  void validate_badPassword_shouldReturnFalse() {
    Assertions.assertThat(verifyThat(badPassword)
                            .compliesWith(passwordRule)
                            .validate()).isFalse();
  }

  @Test
  void orElseThrow_badPassword_shouldThrowException() {
    var validation = verifyThat(badPassword)
      .compliesWith(passwordRule);
    Assertions.assertThatThrownBy(() -> validation.orElseThrow(RuntimeException::new))
              .hasMessageContaining(passwordRuleFailMessage);
  }

  @Test
  void orElseReturn_badPassword_shouldReturnFailMessage() {
    Assertions.assertThat(verifyThat(badPassword)
                            .compliesWith(passwordRule)
                            .orElseReturn(message -> message))
              .contains(passwordRuleFailMessage);
  }

  @Test
  void orElseLogMessage_validInput_shouldReturnTrue() {
    var input = "test";
    var rule = defineThatIt(isA(String.class).where(x -> x.equals(input))).otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThat(verifyThat(input).compliesWith(rule).orElseLogMessage()).isTrue();
  }

  @Test
  void orElseLogMessage_invalidInput_shouldReturnFalse() {
    var input = "test";
    var rule = defineThatIt(isA(String.class).where(x -> x.equals("something else"))).otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThat(verifyThat(input).compliesWith(rule).orElseLogMessage()).isFalse();
  }

  @Test
  void alphabeticInputValidationUseCase_validInput_shouldNotThrowException() {
    var alphabeticStringRule1 = Definitions.defineThatIt(StringPredicate.isAlphabetic()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule2 = Definitions.defineThatIt(StringPredicate.isNotNumeric()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule3 = Definitions.defineThatIt(StringPredicate.isAlphabetic()
                                                                        .and(StringPredicate.isNotNumeric()))
                                           .otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThatNoException().isThrownBy(
      () -> verifyThat("test").compliesWith(alphabeticStringRule1)
                              .and(alphabeticStringRule2)
                              .and(alphabeticStringRule3)
                              .andThen(super::interact)
                              .orElseThrow(RuntimeException::new));
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }

  @Test
  void alphabeticInputValidationUseCase_invalidInput_shouldThrowException() {
    var alphabeticStringRule1 = Definitions.defineThatIt(StringPredicate.isAlphabetic()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule2 = Definitions.defineThatIt(StringPredicate.isNotNumeric()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule3 = Definitions.defineThatIt(StringPredicate.isAlphabetic()
                                                                        .and(StringPredicate.isNotNumeric()))
                                           .otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThatThrownBy(
                () -> verifyThat("12345").compliesWith(alphabeticStringRule1)
                                         .and(alphabeticStringRule2)
                                         .and(alphabeticStringRule3)
                                         .andThen(super::interact)
                                         .orElseThrow(RuntimeException::new))
              .isInstanceOf(RuntimeException.class)
              .hasMessageContaining(FAIL_MESSAGE);
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }

  @Test
  void validateAgainstOtherInputUseCase_validInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);

    Assertions.assertThatNoException().isThrownBy(() -> verifyThat(new TestObject()).compliesWith(rule)
                                                                                    .whileApplying(DEFAULT_MESSAGE)
                                                                                    .and(rule2)
                                                                                    .andThen(super::interact)
                                                                                    .orElseThrow(RuntimeException::new));
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }

  @Test
  void validateAgainstOtherInputUseCase_invalidInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);

    Assertions.assertThatThrownBy(() -> verifyThat(new TestObject()).compliesWith(rule)
                                                                    .whileApplying("Something else entirely")
                                                                    .and(rule2)
                                                                    .andThen(super::interact)
                                                                    .orElseThrow(RuntimeException::new))
              .isInstanceOf(RuntimeException.class)
              .hasMessageContaining(FAIL_MESSAGE);
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }

  @Test
  void validateAgainstOtherInputUsingConsumerUseCase_validInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThatNoException().isThrownBy(() -> verifyThat(input).compliesWith(rule)
                                                                         .whileApplying(DEFAULT_MESSAGE)
                                                                         .and(rule2)
                                                                         .andThen(x -> {
                                                                           Assertions.assertThat(x).isEqualTo(input);
                                                                           super.interact();
                                                                         })
                                                                         .orElseThrow(RuntimeException::new));
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }


  @Test
  void validateAgainstOtherInputUsingConsumerUseCase_invalidInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThatThrownBy(() -> verifyThat(input).compliesWith(rule)
                                                         .whileApplying("Something else entirely")
                                                         .and(rule2)
                                                         .andThen(x -> {
                                                           super.interact();
                                                         })
                                                         .orElseThrow(RuntimeException::new))
              .isInstanceOf(RuntimeException.class)
              .hasMessageContaining(FAIL_MESSAGE);
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }

  @Test
  void validateAgainstOtherInputUsingFunctionUseCase_validInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThatNoException().isThrownBy(
      () -> Assertions.assertThat(verifyThat(input).compliesWith(rule)
                                                   .whileApplying(DEFAULT_MESSAGE)
                                                   .and(rule2)
                                                   .andThen(x -> {
                                                     Assertions.assertThat(x).isEqualTo(input);
                                                     return super.interactAndReturn();
                                                   })
                                                   .orElseThrow(RuntimeException::new)).isTrue());
    Assertions.assertThat(hasBeenInteractedWith()).isTrue();
  }

  @Test
  void validateAgainstOtherInputUsingFunctionUseCase_invalidInput_shouldNotThrowException() {
    var rule = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var rule2 = defineThatIt(isA(TestObject.class).that((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThatThrownBy(
                () -> Assertions.assertThat(verifyThat(input).compliesWith(rule)
                                                             .whileApplying("Something else yet again")
                                                             .and(rule2)
                                                             .andThen(x -> {
                                                               return super.interactAndReturn();
                                                             })
                                                             .orElseThrow(RuntimeException::new)).isFalse())
              .isInstanceOf(RuntimeException.class)
              .hasMessageContaining(FAIL_MESSAGE);
    Assertions.assertThat(hasBeenInteractedWith()).isFalse();
  }

  @Test
  void validateUsingDirectObjectPredicateUseCase_validInput_shouldReturnTrue() {
    var rule2 = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThat(verifyThatIt().isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2).test(input, DEFAULT_MESSAGE)).isTrue();
  }

  @Test
  void validateUsingDirectPredicateUseCases_invalidInput_shouldReturnFalse() {
    var rule2 = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThat(verifyThatIt().isA(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, "Something else")).isFalse();
  }

  @Test
  void validateUsingDirectPredicateUseCases_validInput_shouldReturnTrue() {
    var rule2 = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThat(verifyThatIt().isA(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, DEFAULT_MESSAGE)).isTrue();
    Assertions.assertThat(verifyThatIt().isAn(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, DEFAULT_MESSAGE)).isTrue();
    Assertions.assertThat(verifyThatIt().isNotEqualTo(TestObject.class).test(input)).isTrue();
    Assertions.assertThat(verifyThatIt().isEqualTo(input).test(input)).isTrue();
    Assertions.assertThat(verifyThatIt().isADecimalNumberEqualTo(.0).test(.0)).isTrue();
    Assertions.assertThat(verifyThatIt().isADecimalNumberNotEqualTo(.0).test(.1)).isTrue();
    Assertions.assertThat(verifyThatIt().isAnIntegerEqualTo(1).test(1)).isTrue();
    Assertions.assertThat(verifyThatIt().isAnIntegerNotEqualTo(1).test(2)).isTrue();
    Assertions.assertThat(verifyThatIt().isADecimalNumberContaining(1).test(.1)).isTrue();
    Assertions.assertThat(verifyThatIt().isAnIntegerContaining(1).test(1)).isTrue();
    Assertions.assertThat(verifyThatIt().isAnIntegerNotContaining(1).test(2)).isTrue();
    Assertions.assertThat(verifyThatIt().isADecimalNumberNotContaining(1).test(.2)).isTrue();
    Assertions.assertThat(verifyThatIt().isAnIntegerBetween(1).and(2).test(1)).isTrue();
    Assertions.assertThat(verifyThatIt().isADecimalNumberBetween(.1).and(.2).test(.1)).isTrue();
    Assertions.assertThat(verifyThatIt().isNull().test(null)).isTrue();
    Assertions.assertThat(verifyThatIt().isNotNull().test("test")).isTrue();
  }

  @Test
  void validateUsingDirectPredicateUseCase_invalidInput_shouldReturnFalse() {
    var rule2 = defineThatIt(isA(TestObject.class).where((x, y) -> x.getMessage().equals(y))).otherwiseReport(
      FAIL_MESSAGE);
    var input = new TestObject();
    Assertions.assertThat(verifyThatIt().isA(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, "Something else")).isFalse();
    Assertions.assertThat(verifyThatIt().isAn(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, "Something else")).isFalse();
    Assertions.assertThat(verifyThatIt().isNotEqualTo(input).test(input)).isFalse();
    Assertions.assertThat(verifyThatIt().isEqualTo(input).test(new TestObject())).isFalse();
    Assertions.assertThat(verifyThatIt().isADecimalNumberEqualTo(.0).test(.10)).isFalse();
    Assertions.assertThat(verifyThatIt().isADecimalNumberNotEqualTo(.0).test(.0)).isFalse();
    Assertions.assertThat(verifyThatIt().isAnIntegerEqualTo(1).test(2)).isFalse();
    Assertions.assertThat(verifyThatIt().isAnIntegerNotEqualTo(1).test(1)).isFalse();
    Assertions.assertThat(verifyThatIt().isADecimalNumberContaining(1).test(.2)).isFalse();
    Assertions.assertThat(verifyThatIt().isAnIntegerContaining(1).test(3)).isFalse();
    Assertions.assertThat(verifyThatIt().isAnIntegerNotContaining(1).test(1)).isFalse();
    Assertions.assertThat(verifyThatIt().isADecimalNumberNotContaining(1).test(.1)).isFalse();
    Assertions.assertThat(verifyThatIt().isAnIntegerBetween(1).and(2).test(3)).isFalse();
    Assertions.assertThat(verifyThatIt().isADecimalNumberBetween(.1).and(.2).test(.4)).isFalse();
    Assertions.assertThat(verifyThatIt().isNull().test(input)).isFalse();
    Assertions.assertThat(verifyThatIt().isNotNull().test(null)).isFalse();
  }

  @Test
  void validateUsingPassedPredicateUseCase_validInput_shouldReturnTrue() {
    var input = new TestObject();
    var result = verifyThat(input, DEFAULT_MESSAGE, isA(TestObject.class)
      .where((x, y) -> x.getMessage().equals(y))).otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void validateUsingPassedPredicateUseCase_invalidInput_shouldReturnFalse() {
    var input = new TestObject();
    var result = verifyThat(input, "Something else yet again", isA(TestObject.class)
      .where((x, y) -> x.getMessage().equals(y))).otherwiseReport(FAIL_MESSAGE);
    Assertions.assertThat(result).isFalse();
  }
}
