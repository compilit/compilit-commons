package com.compilit.cryptography;

class CryptographyDefaults {
  static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
  static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
  static final String SECRET_KEY_SPEC_ALGORITHM = "AES";
  static final int IV_LENGTH = 16;
  static final int SALT_LENGTH = 16;
  static final int KEY_LENGTH = 256;
  static final int ITERATION_COUNT = 65536;
}
