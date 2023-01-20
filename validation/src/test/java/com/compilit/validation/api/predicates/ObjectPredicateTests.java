package com.compilit.validation.api.predicates;

import com.compilit.validation.Definitions;
import com.compilit.validation.Verifications;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testutil.TestObject;


class ObjectPredicateTests {

  @Test
  void beA_String_shouldReturnObjectPredicate() {
    var rule = Definitions.defineThatIt(ObjectPredicate.isA(String.class).where(x -> x.contains("test"))).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat("test").compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beAn_Object_shouldReturnObjectPredicate() {
    var actualObject = new Object();
    var rule = Definitions.defineThatIt(ObjectPredicate.isAn(Object.class).where(x -> x.equals(actualObject))).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beA_TestObjectWithoutValues_shouldReturnTrue() {
    var actualObject = new TestObject();
    actualObject.add("test");
    var rule = Definitions.defineThatIt(ObjectPredicate.isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beA_TestObjectWithValues_shouldReturnOtherValue() {
    var failureMessage = "Something went wrong";
    var actualObject = new TestObject();
    var otherObject = new TestObject();
    var rule = Definitions.defineThatIt(ObjectPredicate.isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport(failureMessage);
    var rule2 = Definitions.defineThatIt(ObjectPredicate.isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport(failureMessage);
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(otherObject))
              .isEqualTo(otherObject);
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(otherObject))
              .isNotEqualTo(actualObject);
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(x -> otherObject))
              .isNotEqualTo(actualObject);
    Assertions.assertThat(Verifications.verifyThat(actualObject).compliesWith(rule)
                                       .and(rule2)
                                       .orElseReturn(TestObject::new)
                                       .getMessage()).contains(failureMessage);
  }

  @Test
  void isEqualTo_validInput_shouldReturnTrue() {
    Assertions.assertThat(ObjectPredicate.isEqualTo("test").test("test")).isTrue();
  }

  @Test
  void isEqualTo_invalidInput_shouldReturnFalse() {
    Assertions.assertThat(ObjectPredicate.isEqualTo("test").test("Something else")).isFalse();
  }

  @Test
  void isNotEqualTo_validInput_shouldReturnTrue() {
    Assertions.assertThat(ObjectPredicate.isNotEqualTo("test").test("Something else")).isTrue();
  }

  @Test
  void isNotEqualTo_invalidInput_shouldReturnFalse() {
    Assertions.assertThat(ObjectPredicate.isNotEqualTo("test").test("test")).isFalse();
  }

  @Test
  void isNotNull_shouldAddNotNullPredicate() {
    var rule = Definitions.defineThatIt(ObjectPredicate.isNotNull()).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat(null).compliesWith(rule).validate()).isFalse();
    Assertions.assertThat(Verifications.verifyThat(new Object()).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void contains_singleInput_shouldAssessIfCollectionContainsValue() {
    var value = "test";
    var list = new ArrayList<String>();
    list.add(value);
    var rule = Definitions.defineThatIt(ObjectPredicate.contains(value)).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat((Object) list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void contains_collectionInput_shouldAssessIfCollectionContainsCollection() {
    var value = "test";
    var list = new ArrayList<String>();
    list.add(value);
    var rule = Definitions.defineThatIt(ObjectPredicate.contains(list)).otherwiseReport("failure");
    Assertions.assertThat(Verifications.verifyThat((Object) list).compliesWith(rule).validate()).isTrue();
  }
}
