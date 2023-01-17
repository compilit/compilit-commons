package com.compilit.validation.api;

import static testutil.TestValue.TEST_CONTENT;

import com.compilit.validation.predicates.StringPredicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class VerificationsTests {

  @Test
  void makeSure_shouldReturnNewValidatorEntryPoint() {
    Assertions.assertThat(Verifications.verifyThat(TEST_CONTENT)).isInstanceOf(ValidationBuilder.class);
  }

  @Test
  void completeRule_formattedMessage_shouldReturnFormattedMessage() {
    var ruleFailMessage = "%s does not contain 123!";
    var value = "4";
    var rule1 = Definitions.defineThatIt(StringPredicate.contains("123")).otherwiseReport(ruleFailMessage, value);
    var rule2 = Definitions.defineThatIt(StringPredicate.hasALengthOf(1)).otherwiseReport(ruleFailMessage, value);
    var expectedMessage = String.format(ruleFailMessage, value);
    var validator = Verifications.verifyThat(value).compliesWith(rule1).and(rule2);
    validator.validate();
    var actual = validator.getMessage();
    Assertions.assertThat(actual).contains(expectedMessage);
  }

}
