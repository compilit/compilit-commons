package com.compilit.functions;

/**
 * The general exception which will be thrown for every invalid input for the fuzzy matching functions
 */
public final class MatcherInputException extends RuntimeException {

  MatcherInputException(String message) {
    super(message);
  }
}
