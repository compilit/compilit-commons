package com.compilit.functions;

/**
 * These functions are part of an ongoing attempt to provide solid input sanitizing
 */
public final class Sanitizers {

  private Sanitizers() {}

  /**
   * First, simple version, to be extended Removes non-printable characters except for '\n', '\r' and '\t'
   *
   * @param input the String value you wish to sanitize
   * @return the sanitized String
   */
  public static String softSanitize(String input) {
    if (input == null) {
      return input;
    }
    return sanitize(input, "[^\\n\\r\\t\\P{Print}]");
  }

  /**
   * First, simple version, to be extended Removes non-printable characters
   *
   * @param input the String value you wish to sanitize
   * @return the sanitized String
   */
  public static String sanitize(String input) {
    if (input == null) {
      return input;
    }
    return sanitize(input, "\\P{Print}").trim();
  }

  private static String sanitize(String input, String regex) {
    return input.replaceAll(regex, "");
  }

}

