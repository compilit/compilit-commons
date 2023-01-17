package com.compilit.validation.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidatingRuleDefinitionBuilderTests {

  @Test
  void otherwiseReport_validInput_shouldReturnTrue() {
    var underTest = new ValidatingRuleDefinitionBuilder<>("test", x -> true);
    Assertions.assertThat(underTest.otherwiseReport("test")).isTrue();
  }

  @Test
  void otherwiseReport_invalidInput_shouldReturnFalse() {
    var underTest = new ValidatingRuleDefinitionBuilder<>("test", x -> false);
    Assertions.assertThat(underTest.otherwiseReport("test")).isFalse();
  }

  static class WithDualInputTests {

    @Test
    void otherwiseReport_validInput_shouldReturnTrue() {
      var underTest = new ValidatingRuleDefinitionBuilder.WithDualInput<>("test", "test", (x, y) -> true);
      Assertions.assertThat(underTest.otherwiseReport("test")).isTrue();
    }

    @Test
    void otherwiseReport_invalidInput_shouldReturnFalse() {
      var underTest = new ValidatingRuleDefinitionBuilder.WithDualInput<>("test", "test", (x, y) -> false);
      Assertions.assertThat(underTest.otherwiseReport("test")).isFalse();
    }

  }
}
