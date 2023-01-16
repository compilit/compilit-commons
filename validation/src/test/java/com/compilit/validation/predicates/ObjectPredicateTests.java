package com.compilit.validation.predicates;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testutil.TestObject;

import java.util.ArrayList;

import static com.compilit.validation.api.Definitions.defineThatIt;
import static com.compilit.validation.api.Verifications.verifyThat;
import static com.compilit.validation.predicates.ObjectPredicate.*;


class ObjectPredicateTests {

  @Test
  void beA_String_shouldReturnObjectPredicate() {
    var rule = defineThatIt(isA(String.class).where(x -> x.contains("test"))).otherwiseReport("failure");
    Assertions.assertThat(verifyThat("test").compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beAn_Object_shouldReturnObjectPredicate() {
    var actualObject = new Object();
    var rule = defineThatIt(isAn(Object.class).where(x -> x.equals(actualObject))).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beA_TestObjectWithoutValues_shouldReturnTrue() {
    var actualObject = new TestObject();
    actualObject.add("test");
    var rule = defineThatIt(isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void beA_TestObjectWithValues_shouldReturnOtherValue() {
    var failureMessage = "Something went wrong";
    var actualObject = new TestObject();
    var otherObject = new TestObject();
    var rule = defineThatIt(isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport(failureMessage);
    var rule2 = defineThatIt(isA(TestObject.class).where(TestObject::hasValues)).otherwiseReport(failureMessage);
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(otherObject)).isEqualTo(otherObject);
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(otherObject)).isNotEqualTo(actualObject);
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(x -> otherObject)).isNotEqualTo(actualObject);
    Assertions.assertThat(verifyThat(actualObject).compliesWith(rule).and(rule2).orElseReturn(TestObject::new).getMessage()).contains(failureMessage);
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
    var rule = defineThatIt(isNotNull()).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(null).compliesWith(rule).validate()).isFalse();
    Assertions.assertThat(verifyThat(new Object()).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void contains_singleInput_shouldAssessIfCollectionContainsValue() {
    var value = "test";
    var list = new ArrayList<String>();
    list.add(value);
    var rule = defineThatIt(contains(value)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat((Object)list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void contains_collectionInput_shouldAssessIfCollectionContainsCollection() {
    var value = "test";
    var list = new ArrayList<String>();
    list.add(value);
    var rule = defineThatIt(contains(list)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat((Object)list).compliesWith(rule).validate()).isTrue();
  }
}
