package com.compilit.validation.predicates;

import com.compilit.validation.api.Verifications;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.compilit.validation.api.Definitions.defineThatIt;
import static com.compilit.validation.api.Verifications.verifyThat;
import static com.compilit.validation.predicates.NumberPredicate.*;
import static com.compilit.validation.predicates.ObjectPredicate.contains;

class NumberPredicateBuilderTests {

  @Test
  void validate_integerMatchingRule_shouldReturnTrue() {
    var value = 2;
    var rule0 = defineThatIt(isAnIntegerEqualTo(2)).otherwiseReport("failure");

    var rule1 = defineThatIt(isAnIntegerBetween(1).and(5)).otherwiseReport("failure");
    var rule2 = defineThatIt(isAnIntegerBetween(5).and(1)).otherwiseReport("failure");
    var rule3 = defineThatIt(isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure");
    var rule4 = defineThatIt(isAnIntegerContaining(2, 2)).otherwiseReport("failure");
    var rule5 = defineThatIt(isAnIntegerNotContaining(43, 123)).otherwiseReport("failure");
    var rule6 = defineThatIt(isNotNull()).otherwiseReport("failure");
    var rule7 = defineThatIt(isAnIntegerEqualTo(2)
            .and(isAnIntegerEqualTo(2)
                    .and(isAnIntegerEqualTo(2)))).otherwiseReport("failure");
    var rule8 = defineThatIt(isAnIntegerNotEqualTo(1)).otherwiseReport("failure");
    var rule9 = defineThatIt(isAnIntegerWithAmountOfDigits(1)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule2).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule3).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule4).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule5).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule6).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule7).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule8).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule9).validate()).isTrue();
  }

  @Test
  void validate_integerNotMatchingRule_shouldReturnFalse() {
    var value = 2;
    var rule0 = defineThatIt(isAnIntegerEqualTo(1)).otherwiseReport("failure");
    var rule1 = defineThatIt(isAnIntegerBetween(5).and(50)).otherwiseReport("failure");
    var rule2 = defineThatIt(isAnIntegerBetween(50).and(5)).otherwiseReport("failure");
    var rule3 = defineThatIt(isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure");
    var rule4 = defineThatIt(isAnIntegerEqualTo(5435)).otherwiseReport("failure");
    var rule5 = defineThatIt(isAnIntegerNotContaining(2, 2)).otherwiseReport("failure");
    var rule6 = defineThatIt(isNotNull()).otherwiseReport("failure");
    var rule7 = defineThatIt(isAnIntegerEqualTo(2)
            .and(isAnIntegerEqualTo(2)
                    .and(isAnIntegerEqualTo(1)))).otherwiseReport("failure");
    var rule8 = defineThatIt(isAnIntegerNotEqualTo(2)).otherwiseReport("failure");
    var rule9 = defineThatIt(isAnIntegerWithAmountOfDigits(10)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(value).compliesWith(rule0).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule1).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule2).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule3).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule4).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule5).validate()).isFalse();
    Assertions.assertThat(Verifications.<Integer>verifyThat(null).compliesWith(rule6).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule7).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule8).validate()).isFalse();
    Assertions.assertThat(verifyThat(value).compliesWith(rule9).validate()).isFalse();
  }

  @Test
  void validate_dualInputTests() {
    var value = 123456789;
    var rule1 = defineThatIt(isAnIntegerContaining(1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 23, 789)).otherwiseReport("failure");
    var rule2 = defineThatIt(isAnIntegerContaining(10,
            11,
            22,
            33,
            44,
            55,
            66,
            77,
            88,
            9999)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(value).compliesWith(rule1).validate()).isTrue();
    Assertions.assertThat(verifyThat(value).compliesWith(rule2).validate()).isFalse();
  }
}
