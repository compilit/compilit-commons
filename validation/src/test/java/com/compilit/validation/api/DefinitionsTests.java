package com.compilit.validation.api;

import com.compilit.core.api.validation.Rule;
import com.compilit.validation.predicates.ObjectPredicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testutil.TestObject;

class DefinitionsTests {

  @Test
  void thatIt_shouldReturnCorrectBuilder() {
    var rule1 = Definitions.defineThatIt((x) -> true).otherwiseReport("fail");
    Assertions.assertThat(rule1).isInstanceOf(Rule.class);
    var rule2 = Definitions.defineThatIt(ObjectPredicate.isA(TestObject.class).where((x) -> true))
                           .otherwiseReport("fail");
    Assertions.assertThat(rule2).isInstanceOf(Rule.class);
    var ruleWithDualInput1 = Definitions.defineThatIt((x, y) -> true).otherwiseReport("fail");
    Assertions.assertThat(ruleWithDualInput1).isInstanceOf(Rule.WithDualInput.class);
    var ruleWithDualInput2 = Definitions.defineThatIt(ObjectPredicate.isA(TestObject.class)
                                                                     .where((x, y) -> x.getMessage().equals(y)))
                                        .otherwiseReport("fail");
    Assertions.assertThat(ruleWithDualInput2).isInstanceOf(Rule.WithDualInput.class);
  }
}
