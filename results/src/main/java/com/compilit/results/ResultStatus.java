package com.compilit.results;

/**
 * A status enum for all results.
 */
public enum ResultStatus {
  /**
   * Status for all successful results.
   */
  SUCCESS,
  /**
   * Generic status for all unsuccessful results.
   */
  UNPROCESSABLE,
  /**
   * Generic status for all unsuccessful results with authorization as root cause.
   */
  UNAUTHORIZED,
  /**
   * Generic status for all unsuccessful results with 'not found' issues as root cause. Indicates a non-existent
   * resource.
   */
  NOT_FOUND,
  /**
   * Generic status for all unsuccessful results with an actual exception as root cause.
   */
  ERROR_OCCURRED
}
