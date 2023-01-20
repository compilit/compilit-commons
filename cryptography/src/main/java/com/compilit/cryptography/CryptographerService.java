package com.compilit.cryptography;

import com.compilit.cryptography.api.Cryptographer;
import javax.crypto.SecretKey;

/**
 * A basic implementation of the Cryptographer interface
 */
class CryptographerService implements Cryptographer {

  private final int keyLength;
  private final int iterationCount;

  CryptographerService(int keyLength, int iterationCount) {
    this.keyLength = keyLength;
    this.iterationCount = iterationCount;
  }

  @Override
  public SecretKey generateKey(String secret, byte[] salt) {
    return Cryptographer.super.generateKey(secret, salt, keyLength, iterationCount);
  }
}
