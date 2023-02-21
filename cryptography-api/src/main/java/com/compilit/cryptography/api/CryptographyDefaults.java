package com.compilit.cryptography.api;

class CryptographyDefaults {

  static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
  static final int NONCE_LENGTH = 16;
  static final int ITERATION_COUNT = 65536;

  private CryptographyDefaults() {}
}
