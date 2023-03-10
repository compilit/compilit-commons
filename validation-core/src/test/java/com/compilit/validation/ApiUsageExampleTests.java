package com.compilit.validation;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.Verifications.verifyThat;
import static com.compilit.validation.Verifications.verifyThatIt;
import static com.compilit.validation.api.predicates.ObjectPredicate.isA;
import static com.compilit.validation.api.predicates.StringPredicate.contains;
import static com.compilit.validation.api.predicates.StringPredicate.hasALengthBetween;
import static com.compilit.validation.api.predicates.StringPredicate.isAlphabetic;
import static com.compilit.validation.api.predicates.StringPredicate.isNotNumeric;

import com.compilit.validation.api.Messages;
import com.compilit.validation.api.Rule;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutil.AbstractTestWithContext;
import testutil.TestObject;

//todo: remove or refactor
class ApiUsageExampleTests extends AbstractTestWithContext {

  private static final String FAIL_MESSAGE = "I am error";
  private final String passwordRuleFailMessage = "Password did not meet our requirements!";
  private final String goodPassword = "#This%Should*BeAGoodPassword(";
  private final String badPassword = "Whatever";
  private final Rule<String> passwordRule = defineThatIt(contains("#", "%", "*", "(")
                                                           .and(hasALengthBetween(15).and(50)))
    .otherwiseReport(passwordRuleFailMessage);

  @BeforeEach
  public void reset() {
    super.reset();
  }

  @Test
  void validate_goodPassword_shouldReturnTrue() {
    var passwordRuleFailMessage = "Password did not meet our requirements!";

    var passwordRule = defineThatIt(contains("#", "%", "*", "(")
                                      .and(hasALengthBetween(15).and(50)))
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
    var alphabeticStringRule1 = defineThatIt(isAlphabetic()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule2 = defineThatIt(isNotNumeric()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule3 = defineThatIt(isAlphabetic()
                                               .and(isNotNumeric()))
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
    var alphabeticStringRule1 = defineThatIt(isAlphabetic()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule2 = defineThatIt(isNotNumeric()).otherwiseReport(FAIL_MESSAGE);
    var alphabeticStringRule3 = defineThatIt(isAlphabetic()
                                               .and(isNotNumeric()))
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
                                                                                    .whileApplying(Messages.DEFAULT_MESSAGE)
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
                                                                         .whileApplying(Messages.DEFAULT_MESSAGE)
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
      () -> Assertions.assertThat(verifyThat(input)
                                    .compliesWith(rule)
                                    .whileApplying(Messages.DEFAULT_MESSAGE)
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
                                        .and(rule2).test(input, Messages.DEFAULT_MESSAGE)).isTrue();
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
                                        .test(input, Messages.DEFAULT_MESSAGE)).isTrue();
    Assertions.assertThat(verifyThatIt().isAn(TestObject.class)
                                        .where((x, y) -> x.getMessage().equals(y))
                                        .and(rule2)
                                        .test(input, Messages.DEFAULT_MESSAGE)).isTrue();
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

  @ParameterizedTest
  @MethodSource("validateUsingDirectPredicateUseCase")
  void validateUsingDirectPredicateUseCase_false_shouldReturnFalse(Predicate<TestObject> predicate) {
    var input = new TestObject();
    Assertions.assertThat(predicate.test(input)).isFalse();
  }

  @ParameterizedTest
  @MethodSource("numberTestCases")
  void numberTestCases_validInput_shouldReturnFalse(boolean testCase) {
    Assertions.assertThat(testCase).isFalse();
  }


  public static Stream<Arguments> validateUsingDirectPredicateUseCase() {
    return Stream.of(
      Arguments.arguments((Predicate<TestObject>)input -> verifyThatIt().isNotEqualTo(input).test(input)),
      Arguments.arguments((Predicate<TestObject>)input -> verifyThatIt().isNull().test(input)),
      Arguments.arguments((Predicate<TestObject>)input -> verifyThatIt().isNotNull().test(null)),
      Arguments.arguments((Predicate<TestObject>)input -> verifyThatIt().isEqualTo(input).test(new TestObject()))
    );
  }

  private static Stream<Arguments> numberTestCases() {
    return Stream.of(
      Arguments.arguments(verifyThatIt().isADecimalNumberEqualTo(.0).test(.10)),
      Arguments.arguments(verifyThatIt().isADecimalNumberNotEqualTo(.0).test(.0)),
      Arguments.arguments(verifyThatIt().isAnIntegerEqualTo(1).test(2)),
      Arguments.arguments(verifyThatIt().isAnIntegerNotEqualTo(1).test(1)),
      Arguments.arguments(verifyThatIt().isADecimalNumberContaining(1).test(.2)),
      Arguments.arguments(verifyThatIt().isAnIntegerContaining(1).test(3)),
      Arguments.arguments(verifyThatIt().isAnIntegerNotContaining(1).test(1)),
      Arguments.arguments(verifyThatIt().isADecimalNumberNotContaining(1).test(.1)),
      Arguments.arguments(verifyThatIt().isAnIntegerBetween(1).and(2).test(3)),
      Arguments.arguments(verifyThatIt().isADecimalNumberBetween(.1).and(.2).test(.4))
    );
  }


  @Test
  void validateUsingPassedPredicateUseCase_validInput_shouldReturnTrue() {
    var input = new TestObject();
    var result = verifyThat(input, Messages.DEFAULT_MESSAGE, isA(TestObject.class)
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
