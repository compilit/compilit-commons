package com.compilit.results;

/**
 * An exception that can be thrown in case of any unauthorized result.
 */
public class UnauthorizedException extends RuntimeException {

  UnauthorizedException(String message) {
    super(message);
  }
}
