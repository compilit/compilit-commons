package com.compilit.cryptography.api;

public class CryptographyDefaults {

  public static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
  public static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
  public static final String SECRET_KEY_SPEC_ALGORITHM = "AES";
  public static final int NONCE_LENGTH = 16;
  public static final int IV_LENGTH = NONCE_LENGTH;
  public static final int SALT_LENGTH = NONCE_LENGTH;
  public static final int KEY_LENGTH = 256;
  public static final int ITERATION_COUNT = 65536;

  private CryptographyDefaults() {}
}
