package com.compilit.cryptography;

class CryptographyCoreDefaults {

  public static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
  public static final String SECRET_KEY_SPEC_ALGORITHM = "AES";
  public static final int NONCE_LENGTH = 16;
  public static final int IV_LENGTH = NONCE_LENGTH;
  public static final int SALT_LENGTH = NONCE_LENGTH;


  private CryptographyCoreDefaults() {}
}
