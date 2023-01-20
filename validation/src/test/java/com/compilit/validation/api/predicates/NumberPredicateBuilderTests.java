package com.compilit.validation.api.predicates;

import com.compilit.validation.Definitions;
import com.compilit.validation.Verifications;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NumberPredicateBuilderTests {

  @Test
  void validate_integerMatchingRule_shouldReturnTrue() {
    var value = 2;
    var rule0 = Definitions.defineThatIt(NumberPredicate.isAnIntegerEqualTo(2)).otherwiseReport("failure");

    var rule1 = Definitions.defineThatIt(NumberPredicate.isAnIntegerBetween(1).and(5)).otherwiseReport("failure");
    var rule2 = Definitions.defineThatIt(NumberPredicate.isAnIntegerBetween(5).and(1)).otherwiseReport("failure");
    var rule3 = Definitions.defineThatIt(NumberPredicate.isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure");
    var rule4 = Definitions.defineThatIt(NumberPredicate.isAnIntegerContaining(2, 2)).otherwiseReport("failure");
    var rule5 = Definitions.defineThatIt(NumberPredicate.isAnIntegerNotContaining(43, 123)).otherwiseReport("failure");
    var rule6 = Definitions.defineThatIt(NumberPredicate.isNotNull()).otherwiseReport("failure");
    var rule7 = Definitions.defineThatIt(NumberPredicate.isAnIntegerEqualTo(2)
                                                        .and(NumberPredicate.isAnIntegerEqualTo(2)
                                                                            .and(NumberPredicate.isAnIntegerEqualTo(2)))).otherwiseReport("failure");
    var rule8 = Definitions.defineThatIt(NumberPredicate.isAnIntegerNotEqualTo(1)).otherwiseReport("failure");
    var rule9 = Definitions.defineThatIt(NumberPredicate.isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule0).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule2).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule3).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule4).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule5).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule6).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule7).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule8).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule9).validate()).isTrue();
  }

  @Test
  void validate_integerNotMatchingRule_shouldReturnFalse() {
    var value = 2;
    var rule0 = Definitions.defineThatIt(NumberPredicate.isAnIntegerEqualTo(1)).otherwiseReport("failure");
    var rule1 = Definitions.defineThatIt(NumberPredicate.isAnIntegerBetween(5).and(50)).otherwiseReport("failure");
    var rule2 = Definitions.defineThatIt(NumberPredicate.isAnIntegerBetween(50).and(5)).otherwiseReport("failure");
    var rule3 = Definitions.defineThatIt(NumberPredicate.isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure");
    var rule4 = Definitions.defineThatIt(NumberPredicate.isAnIntegerEqualTo(5435)).otherwiseReport("failure");
    var rule5 = Definitions.defineThatIt(NumberPredicate.isAnIntegerNotContaining(2, 2)).otherwiseReport("failure");
    var rule6 = Definitions.defineThatIt(NumberPredicate.isNotNull()).otherwiseReport("failure");
    var rule7 = Definitions.defineThatIt(NumberPredicate.isAnIntegerEqualTo(2)
                                                        .and(NumberPredicate.isAnIntegerEqualTo(2)
                                                                            .and(NumberPredicate.isAnIntegerEqualTo(1)))).otherwiseReport("failure");
    var rule8 = Definitions.defineThatIt(NumberPredicate.isAnIntegerNotEqualTo(2)).otherwiseReport("failure");
    var rule9 = Definitions.defineThatIt(NumberPredicate.isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule0).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule1).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule2).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule3).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule4).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule5).validate()).isFalse();
    Assertions.assertThat(Verifications.<Integer>verifyThat(null).compliesWith(rule6).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule7).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule8).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule9).validate()).isFalse();
  }

  @Test
  void validate_dualInputTests() {
    var value = 123456789;
    var rule1 = Definitions.defineThatIt(NumberPredicate.isAnIntegerContaining(1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 23, 789)).otherwiseReport("failure");
    var rule2 = Definitions.defineThatIt(NumberPredicate.isAnIntegerContaining(
      10,
      11,
      22,
      33,
      44,
      55,
      66,
      77,
      88,
      9999
    )).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(Verifications.verifyThat(value).compliesWith(rule2).validate()).isFalse();
  }
}
