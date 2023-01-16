package com.compilit.validation.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ObjectPredicate<T> implements Predicate<T>, PredicateAppender<T> {

  private final List<Predicate<T>> predicates = new ArrayList<>();

  ObjectPredicate() {
  }

  protected ObjectPredicate(Predicate<T> predicate) {
    predicates.add(predicate);
  }

  /**
   * @param clazz the class of the object to validate. This is only a compiler flag to be able to treat T as its
   *              instance.
   * @param <T>   the type upon which the validations are tested.
   * @return GenericPredicateRule to continue adding rules.
   */
  @SuppressWarnings("unused")
  public static <T> PredicateAppender<T> isA(final Class<T> clazz) {
    return new ObjectPredicate<>();
  }

  /**
   * @param clazz the class of the object to validate. This is only a compiler flag to be able to treat T as its
   *              instance.
   * @param <T>   the type upon which the validations are tested.
   * @return GenericPredicateRule to continue adding rules.
   */
  public static <T> PredicateAppender<T> isAn(final Class<T> clazz) {
    return isA(clazz);
  }

  /**
   * Check if the actual value is equal to the given one.
   *
   * @param value the exact expected value.
   * @param <T>   the value which you wish to test against the original value.
   * @return Predicate to continue adding rules.
   */
  public static <T> Predicate<T> isEqualTo(T value) {
    return new ObjectPredicate<>(x -> Objects.equals(x, value));
  }

  /**
   * Check if the actual value is not equal to the given one.
   *
   * @param value the exact expected value.
   * @param <T>   the value which you wish to test against the original value.
   * @return Predicate to continue adding rules.
   */
  public static <T> Predicate<T> isNotEqualTo(T value) {
    return isEqualTo(value).negate();
  }

  /**
   * check whether the given value is not null.
   *
   * @param <T> the value which you wish to test against the original value.
   * @return a predicate to continue adding predicates or to start your evaluation.
   */
  public static <T> Predicate<T> isNotNull() {
    return new ObjectPredicate<>(Objects::nonNull);
  }

  /**
   * check whether the given value is null.
   *
   * @param <T> the value which you wish to test against the original value.
   * @return a predicate to continue adding predicates or to start your evaluation.
   */
  public static <T> Predicate<T> isNull() {
    return new ObjectPredicate<>(Objects::isNull);
  }

  /**
   * If the value under test is a Collection, it will check the collection for containment of the given value(s).
   * Otherwise, it will take the toString value of the value and checks is for containment of the toStrings of the given
   * value(s).
   *
   * @param value  the value of which you wish to check if it is contained in the value under test.
   * @param values the optional remaining values of which you wish to check if it is contained in the value under test.
   * @param <T>    the type of the object under test.
   * @return a predicate to continue adding predicates or to start your evaluation.
   */
  public static <T> Predicate<T> contains(Object value, Object... values) {
    Predicate<T> collectionTest = x -> x instanceof Collection<?>;
    var allowedValues = getAllowedValues(value, values);
    collectionTest = collectionTest.and(collectionContains(allowedValues));
    Predicate<T> predicate = x -> x.toString().contains(value.toString());
    for (final var object : values) {
      predicate = predicate.and(x -> x.toString().contains(object.toString()));
    }
    return new ObjectPredicate<>(collectionTest.or(predicate));
  }

  /**
   * If the value under test is a Collection, it will check the collection for containment of only the given value(s).
   * Otherwise, it will take the toString value of the value and checks is for containment of only the toStrings of the
   * given value(s) or the containment of only the toString values in the toString of the value under test as a char[].
   *
   * @param value  the value of which you wish to check if it is contained in the value under test.
   * @param values the optional remaining values of which you wish to check if it is contained in the value under test.
   * @param <T>    the type of the object under test.
   * @return a predicate to continue adding predicates or to start your evaluation.
   */
  public static <T> Predicate<T> containsOnly(Object value, Object... values) {
    var allowedValues = getAllowedValues(value, values);
    Predicate<T> itIsACollection = x -> x instanceof Collection<?>;
    Predicate<T> itIsACollectionContainingOnlyAllowedValues = itIsACollection.and(collectionContainsOnly(allowedValues));
    return new ObjectPredicate<>(itIsACollectionContainingOnlyAllowedValues.or(itContainsOnly(allowedValues)));
  }

  private static <T> Predicate<T> itContainsOnly(List<Object> allowedValues) {
    return input -> {
      var containsOnlyAllowedCharacters = true;
      var inputChars = getInputCharacters(input);
      var allowedStringValues = allowedValues.stream().map(String::valueOf).toList();
      for (var c : inputChars) {
        if (!allowedStringValues.contains(String.valueOf(c))) {
          containsOnlyAllowedCharacters = false;
          break;
        }
      }
      var containsOnlyAllowedString = allowedValues.contains(input.toString());
      return containsOnlyAllowedCharacters || containsOnlyAllowedString;
    };
  }

  private static <T> char[] getInputCharacters(T input) {
    if (input instanceof Double) {
      return input.toString().replace(".", "").toCharArray();
    } else return  input.toString().toCharArray();
  }

  /**
   * @param value  the value of which you wish to check if it is not contained in the value under test.
   * @param values the optional remaining values of which you wish to check if it is not contained in the value under
   *               test.
   * @param <T>    the type of the object under test.
   * @return a predicate to continue adding predicates or to start your evaluation.
   * @see ObjectPredicate#contains
   */
  public static <T> Predicate<T> doesNotContain(Object value, Object... values) {
    Predicate<T> predicate = x -> true;
    predicate = predicate.and(contains(value, values).negate());
    return new ObjectPredicate<>(predicate);
  }

  @Override
  public boolean test(T value) {
    return predicates.stream().allMatch(x -> x.test(value));
  }

  private static <T> Predicate<T> collectionContains(List<Object> allowedValues) {
    Predicate<T> predicate = x -> true;
    for (final var object : allowedValues) {
      predicate = predicate.and(x -> ((Collection<?>) x).contains(object));
    }
    return predicate;
  }

  private static <T> Predicate<T> collectionContainsOnly(List<Object> allowedValues) {
    return collection -> {
      for (final var object : (Collection<?>) collection) {
        if (!allowedValues.contains(object)) {
          return false;
        }
      }
      return true;
    };
  }

  private static List<Object> getAllowedValues(Object value, Object[] values) {
    final List<Object> allowedValues = new ArrayList<>();
    if (values != null && values.length > 0) {
      allowedValues.add(value);
      allowedValues.addAll(Arrays.asList(values));
    } else {
      allowedValues.add(value);
    }
    return allowedValues;
  }

}
