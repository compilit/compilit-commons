package com.compilit.validation.api.predicates;

import static com.compilit.validation.Definitions.defineThatIt;
import static com.compilit.validation.Verifications.verifyThat;
import static com.compilit.validation.api.predicates.CollectionPredicate.isACollectionContaining;
import static com.compilit.validation.api.predicates.CollectionPredicate.isACollectionContainingAll;
import static com.compilit.validation.api.predicates.CollectionPredicate.isACollectionNotContaining;
import static com.compilit.validation.api.predicates.CollectionPredicate.isACollectionNotContainingAll;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAListContaining;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAListContainingAll;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAListNotContaining;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAListNotContainingAll;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAnEmptyCollectionOf;
import static com.compilit.validation.api.predicates.CollectionPredicate.isAnEmptyList;
import static com.compilit.validation.api.predicates.CollectionPredicate.isNotAnEmptyCollection;
import static com.compilit.validation.api.predicates.CollectionPredicate.isNotAnEmptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CollectionPredicateTests {

  @Test
  void isACollectionContaining_validInput_shouldReturnTrue() {
    var value = "test";
    Collection<String> list = new ArrayList<>();
    list.add(value);
    var rule = defineThatIt(isACollectionContaining(value)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isACollectionContaining_invalidInput_shouldReturnFalse() {
    var value = "test";
    Collection<String> list = new ArrayList<>();
    list.add(value);
    var rule = defineThatIt(isACollectionContaining("No such entry")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isACollectionContainingAll_validInput_shouldReturnTrue() {
    var value = "test";
    Collection<String> list = new ArrayList<>();
    list.add(value);
    Collection<String> list2 = new ArrayList<>();
    list2.add(value);
    var rule = defineThatIt(isACollectionContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isACollectionContainingAll_invalidInput_shouldReturnFalse() {
    var value = "test";
    Collection<String> list = new ArrayList<>();
    list.add(value);
    Collection<String> list2 = new ArrayList<>();
    list2.add("Something else");
    var rule = defineThatIt(isACollectionContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAnEmptyCollection_validInput_shouldReturnTrue() {
    Collection<Object> list = new ArrayList<>();
    var rule = defineThatIt(isAnEmptyCollectionOf(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAnEmptyCollection_invalidInput_shouldReturnFalse() {
    var value = "test";
    Collection<String> list = new ArrayList<>();
    list.add(value);
    var rule = defineThatIt(isAnEmptyCollectionOf(String.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAListContainingAll_validInput_shouldReturnTrue() {
    List<Object> list = new ArrayList<>();
    list.add("test");
    List<Object> list2 = new ArrayList<>();
    list2.add("test");
    var rule = defineThatIt(isAListContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAListContainingAll_invalidInput_shouldReturnFalse() {
    List<Object> list = new ArrayList<>();
    list.add("test");
    List<Object> list2 = new ArrayList<>();
    list2.add("something");
    var rule = defineThatIt(isAListContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isNotAnEmptyCollection_validInput_shouldReturnTrue() {
    Collection<Object> list = new ArrayList<>();
    list.add(new Object());
    var rule = defineThatIt(isNotAnEmptyCollection(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isNotAnEmptyCollection_invalidInput_shouldReturnFalse() {
    Collection<Object> list = new ArrayList<>();
    var rule = defineThatIt(isNotAnEmptyCollection(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isNotAnEmptyList_validInput_shouldReturnTrue() {
    List<Object> list = new ArrayList<>();
    list.add(new Object());
    var rule = defineThatIt(isNotAnEmptyList(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isNotAnEmptyList_invalidInput_shouldReturnFalse() {
    List<Object> list = new ArrayList<>();
    var rule = defineThatIt(isNotAnEmptyList(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAnEmptyList_validInput_shouldReturnTrue() {
    List<Object> list = new ArrayList<>();
    var rule = defineThatIt(isAnEmptyList(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAnEmptyList_invalidInput_shouldReturnFalse() {
    List<Object> list = new ArrayList<>();
    list.add(new Object());
    var rule = defineThatIt(isAnEmptyList(Object.class)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAListContaining_validInput_shouldReturnTrue() {
    List<String> list = new ArrayList<>();
    list.add("test");
    var rule = defineThatIt(isAListContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAListContaining_invalidInput_shouldReturnFalse() {
    List<String> list = new ArrayList<>();
    var rule = defineThatIt(isAListContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isACollectionNotContaining_validInput_shouldReturnTrue() {
    Collection<String> list = new ArrayList<>();
    var rule = defineThatIt(isACollectionNotContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isACollectionNotContaining_invalidInput_shouldReturnFalse() {
    Collection<String> list = new ArrayList<>();
    list.add("test");
    var rule = defineThatIt(isACollectionNotContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isACollectionNotContainingAll_validInput_shouldReturnTrue() {
    Collection<String> list = new ArrayList<>();
    list.add("test");
    Collection<String> list2 = new ArrayList<>();
    list2.add("Something else");
    var rule = defineThatIt(isACollectionNotContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isACollectionNotContainingAll_invalidInput_shouldReturnFalse() {
    Collection<String> list = new ArrayList<>();
    Collection<String> list2 = new ArrayList<>();
    list.add("test");
    list2.add("test");
    var rule = defineThatIt(isACollectionNotContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAListNotContaining_validInput_shouldReturnTrue() {
    List<String> list = new ArrayList<>();
    var rule = defineThatIt(isAListNotContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAListNotContaining_invalidInput_shouldReturnFalse() {
    List<String> list = new ArrayList<>();
    list.add("test");
    var rule = defineThatIt(isAListNotContaining("test")).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }

  @Test
  void isAListNotContainingAll_validInput_shouldReturnTrue() {
    List<String> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    list.add("test");
    list2.add("Something else");
    var rule = defineThatIt(isAListNotContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isTrue();
  }

  @Test
  void isAListNotContainingAll_invalidInput_shouldReturnFalse() {
    List<String> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    list.add("test");
    list2.add("test");
    var rule = defineThatIt(isAListNotContainingAll(list2)).otherwiseReport("failure");
    Assertions.assertThat(verifyThat(list).compliesWith(rule).validate()).isFalse();
  }
}
