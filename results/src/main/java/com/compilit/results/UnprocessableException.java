package com.compilit.results;

/**
 * An exception that can be thrown in case of any unprocessable result.
 */
public class UnprocessableException extends RuntimeException {

  UnprocessableException(String message) {
    super(message);
  }
}
