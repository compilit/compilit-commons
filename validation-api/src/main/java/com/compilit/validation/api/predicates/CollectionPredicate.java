package com.compilit.validation.api.predicates;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class CollectionPredicate {

  private CollectionPredicate() {
  }

  /**
   * @param clazz a compiler flag to tell the compiler what kind of items should be in the collection.
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  @SuppressWarnings("unused")
  public static <T> Predicate<Collection<T>> isAnEmptyCollectionOf(Class<T> clazz) {
    return Collection::isEmpty;
  }

  /**
   * @param clazz a compiler flag to tell the compiler what kind of items should be in the .
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  @SuppressWarnings("unused")
  public static <T> Predicate<Collection<T>> isNotAnEmptyCollection(Class<T> clazz) {
    return x -> !x.isEmpty();
  }

  /**
   * @param clazz a compiler flag to tell the compiler what kind of items should be in the list.
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  @SuppressWarnings("unused")
  public static <T> Predicate<List<T>> isAnEmptyList(Class<T> clazz) {
    return Collection::isEmpty;
  }

  /**
   * @param clazz a compiler flag to tell the compiler what kind of items should be in the list.
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  @SuppressWarnings("unused")
  public static <T> Predicate<List<T>> isNotAnEmptyList(Class<T> clazz) {
    return x -> !x.isEmpty();
  }

  /**
   * @param entry element whose presence in this collection is to be tested
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  public static <T> Predicate<Collection<T>> isACollectionContaining(T entry) {
    return x -> x.contains(entry);
  }

  /**
   * @param entry element whose presence in this collection is to be tested
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  public static <T> Predicate<Collection<T>> isACollectionNotContaining(T entry) {
    return isACollectionContaining(entry).negate();
  }

  /**
   * @param entry element whose presence in this collection is to be tested
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  public static <T> Predicate<List<T>> isAListContaining(T entry) {
    return x -> x.contains(entry);
  }

  /**
   * @param entry element whose presence in this collection is to be tested
   * @return a predicate to continue adding predicates or to start the evaluation.
   */
  public static <T> Predicate<List<T>> isAListNotContaining(T entry) {
    return isAListContaining(entry).negate();
  }

  /**
   * @param collection collection to be checked for containment in this collection
   */
  public static <T> Predicate<Collection<T>> isACollectionContainingAll(Collection<T> collection) {
    return x -> x.containsAll(collection);
  }

  /**
   * @param collection collection to be checked for containment in this collection
   */
  public static <T> Predicate<Collection<T>> isACollectionNotContainingAll(Collection<T> collection) {
    return isACollectionContainingAll(collection).negate();
  }

  /**
   * @param list list to be checked for containment in this collection
   */
  public static <T> Predicate<List<T>> isAListContainingAll(List<T> list) {
    return x -> x.containsAll(list);
  }

  /**
   * @param list list to be checked for containment in this collection
   */
  public static <T> Predicate<List<T>> isAListNotContainingAll(List<T> list) {
    return isAListContainingAll(list).negate();
  }

}
