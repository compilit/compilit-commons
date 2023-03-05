package com.compilit.sanitization;

/**
 * These functions are part of an ongoing attempt to provide solid input sanitizing.
 */
public final class Sanitizers {

  private Sanitizers() {}

  /**
   * First, simple version, to be improved. Removes all non-printable characters
   *
   * @param input the String value you wish to sanitize
   * @return the sanitized String
   */
  public static String sanitize(CharSequence input) {
    if (input == null) {
      return null;
    }
    return sanitize(input, "\\P{Print}").trim();
  }

  private static String sanitize(CharSequence input, String regex) {
    return input.toString().replaceAll(regex, "");
  }

}

