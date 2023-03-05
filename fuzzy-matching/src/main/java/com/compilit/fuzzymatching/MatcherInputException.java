package com.compilit.fuzzymatching;

/**
 * The general exception which will be thrown for every invalid input for the fuzzy matching functions
 */
public final class MatcherInputException extends RuntimeException {

  MatcherInputException() {
    super("Matching percentage cannot be below 0 nor exceed 100");
  }
}
