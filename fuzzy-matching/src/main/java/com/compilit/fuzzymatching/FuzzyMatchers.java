package com.compilit.fuzzymatching;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This fuzzy matching algorithm is not a Levenshtein implementation, but rather an approach based on the percentage of
 * matching sequences between two strings. Which is more practical for a lot of use cases. It will check if there are
 * sequences of characters matching between the two values. For any non-matching sequence, the respective sequence value
 * will be subtracted from the matching percentage. This percentage to match for at least the given match percentage, or
 * 80%, which is the default.
 * <p>
 * This implementation is useful when dealing with imprecise results from OCR for example.
 */
public final class FuzzyMatchers {

  static final float MAX_PERCENTAGE = 100;
  static final float DEFAULT_MINIMAL_MATCHING_PERCENTAGE = 80;

  private FuzzyMatchers() {}

  /**
   * Case-sensitive fuzzy matching
   *
   * @param otherValue the second value to try to match
   * @return a Predicate that returns true if both values match for at least 80% (the default matching percentage)
   */
  public static Predicate<String> fuzzyMatches(String otherValue) {
    return value -> fuzzyMatches(value, otherValue);
  }

  /**
   * Case-insensitive fuzzy matching
   *
   * @param otherValue the second value to try to match
   * @return a Predicate that returns true if both values match for at least 80% (the default matching percentage)
   */
  public static Predicate<String> fuzzyMatchesIgnoreCase(String otherValue) {
    return value -> fuzzyMatchesIgnoreCase(value, otherValue);
  }

  /**
   * Case-sensitive fuzzy matching
   *
   * @param otherValue                the second value to try to match
   * @param minimalMatchingPercentage the desired matching percentage between the two values
   * @return a Predicate that returns true if both values match for at least the given matching percentage
   */
  public static Predicate<String> fuzzyMatches(String otherValue, float minimalMatchingPercentage) {
    return value -> fuzzyMatches(value, otherValue, minimalMatchingPercentage);
  }

  /**
   * Case-sensitive fuzzy matching
   *
   * @param value      the first value to try to match
   * @param otherValue the second value to try to match
   * @return true if both values match for at least 80% (the default matching percentage)
   */
  public static boolean fuzzyMatches(String value, String otherValue) {
    return fuzzyMatches(value, otherValue, DEFAULT_MINIMAL_MATCHING_PERCENTAGE);
  }


  /**
   * Case-insensitive fuzzy matching
   *
   * @param value      the first value to try to match
   * @param otherValue the second value to try to match
   * @return true if both values match for at least 80% (the default matching percentage)
   */
  public static boolean fuzzyMatchesIgnoreCase(String value, String otherValue) {
    return fuzzyMatchesIgnoreCase(value, otherValue, DEFAULT_MINIMAL_MATCHING_PERCENTAGE);
  }

  /**
   * Case-insensitive fuzzy matching
   *
   * @param value                     the first value to try to match
   * @param otherValue                the second value to try to match
   * @param minimalMatchingPercentage the desired matching percentage between the two values
   * @return true if both values match for at least the given matching percentage
   */
  public static boolean fuzzyMatchesIgnoreCase(
    String value,
    String otherValue,
    float minimalMatchingPercentage
  ) {
    if (bothValuesAreNullOrEmpty(value, otherValue)) {
      return true;
    }
    if (oneValueIsNullOrEmpty(value, otherValue)) {
      return false;
    }
    value = value.toLowerCase();
    otherValue = otherValue.toLowerCase();
    return fuzzyMatches(value, otherValue, minimalMatchingPercentage);
  }

  /**
   * Case-sensitive fuzzy matching
   *
   * @param value                     the first value to try to match
   * @param otherValue                the second value to try to match
   * @param minimalMatchingPercentage the desired matching percentage between the two values
   * @return true if both values match for at least the given matching percentage
   */
  public static boolean fuzzyMatches(
    String value,
    String otherValue,
    float minimalMatchingPercentage
  ) {
    if (minimalMatchingPercentage > MAX_PERCENTAGE || minimalMatchingPercentage < 0) {
      throw new MatcherInputException();
    }
    if (bothValuesAreNullOrEmpty(value, otherValue)) {
      return true;
    }
    if (oneValueIsNullOrEmpty(value, otherValue)) {
      return false;
    }
    float charSequenceMatchPercentage = getCharSequenceMatchPercentage(value, otherValue);
    return charSequenceMatchPercentage >= minimalMatchingPercentage;
  }

  /**
   * Return the percentage of matching characters between the two values.
   *
   * @param value      the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of characters matching between the two Strings
   */
  public static float getCharMatchPercentage(String value, String otherValue) {
    float shortestValueLength = Math.min(value.length(), otherValue.length());
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float charMatchPercentage = MAX_PERCENTAGE;
    for (int index = 0; index < shortestValueLength; index++) {
      if (!value.contains(String.valueOf(otherValue.charAt(index)))) {
        charMatchPercentage -= percentageDivider;
      }
    }
    return charMatchPercentage;
  }

  /**
   * Return the percentage of matching length between the two values.
   *
   * @param value      the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of the matching length between the two Strings
   */
  public static float getLengthMatchPercentage(
    String value,
    String otherValue
  ) {
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float lengthMatchPercentage = MAX_PERCENTAGE;
    if (value.length() > otherValue.length()) {
      var difference = Math.subtractExact(value.length(), otherValue.length());
      lengthMatchPercentage -= multiply(percentageDivider, difference);
    } else if (otherValue.length() > value.length()) {
      var difference = Math.subtractExact(otherValue.length(), value.length());
      lengthMatchPercentage -= multiply(percentageDivider, difference);
    }
    return lengthMatchPercentage;
  }

  private static boolean oneValueIsNullOrEmpty(String value, String otherValue) {
    return (value == null || otherValue == null)
      || (value.isEmpty() || otherValue.isEmpty());
  }

  private static boolean bothValuesAreNullOrEmpty(String value, String otherValue) {
    return (value == null && otherValue == null)
      || ((value != null && value.isEmpty()) && (otherValue != null && otherValue.isEmpty()));
  }

  private static float multiply(float percentageDivider, int difference) {
    return difference * percentageDivider;
  }

  private static float getCharSequenceMatchPercentage(
    String value,
    String otherValue
  ) {
    int longestValueLength = Math.max(value.length(), otherValue.length());
    float sequenceValue = MAX_PERCENTAGE / longestValueLength;
    float charSequenceMatchPercentage = MAX_PERCENTAGE;
    charSequenceMatchPercentage = getPrimaryCharSequenceMatchPercentage(
      value,
      otherValue,
      sequenceValue,
      charSequenceMatchPercentage
    );
    charSequenceMatchPercentage = getSecondaryCharSequenceMatchPercentage(
      value,
      otherValue,
      sequenceValue,
      charSequenceMatchPercentage
    );
    return charSequenceMatchPercentage;
  }

  private static float getPrimaryCharSequenceMatchPercentage(
    String value,
    String otherValue,
    float sequenceValue,
    float initialMatchingPercentage
  ) {
    int shortestValueLength = Math.min(value.length(), otherValue.length());
    var currentSequence = new StringBuilder();
    for (int index = 0; index < shortestValueLength; index++) {
      currentSequence.append(otherValue.charAt(index));
      if (!value.contains(currentSequence)) {
        initialMatchingPercentage -= sequenceValue;
        currentSequence = new StringBuilder();
      }
    }
    return initialMatchingPercentage;
  }

  private static float getSecondaryCharSequenceMatchPercentage(
    String value,
    String otherValue,
    float sequenceValue,
    float initialMatchingPercentage
  ) {
    int longestValueLength = Math.max(value.length(), otherValue.length());
    int shortestValueLength = Math.min(value.length(), otherValue.length());
    var currentSequence = new StringBuilder();
    for (int index = shortestValueLength; index < longestValueLength; index++) {
      final int tmpIndex = index;
      currentSequence.append(orEmptyString(() -> value.charAt(tmpIndex)));
      if (!otherValue.contains(currentSequence)) {
        initialMatchingPercentage -= sequenceValue;
        currentSequence = new StringBuilder();
      }
    }
    return initialMatchingPercentage;
  }

  private static String orEmptyString(
    Supplier<Character> supplier
  ) {
    try {
      return String.valueOf(supplier.get());
    } catch (Exception e) {
      return "";
    }
  }

}
