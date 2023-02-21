package com.compilit.cryptography;

import com.compilit.cryptography.api.Cryptographer;
import java.io.Serializable;
import javax.crypto.SecretKey;

/**
 * A basic implementation of the Cryptographer interface
 */
class CryptographerService implements Cryptographer {

  private final CryptographerConfiguration cryptographerConfiguration;

  /**
   * Create a default instance of the CryptographerService.
   * Uses a key length of 256 and an iteration count of 65536
   */
  CryptographerService(CryptographerConfiguration cryptographerConfiguration) {
    this.cryptographerConfiguration = cryptographerConfiguration;
  }


  @Override
  public <T extends Serializable> byte[] encrypt(String algorithm, T valueToEncrypt, byte[] salt, SecretKey key) {
    return CryptoFunctions.encrypt(algorithm, valueToEncrypt, salt, key);
  }

  @Override
  public <T extends Serializable> T decrypt(String algorithm, byte[] encryptedValue) {
    return CryptoFunctions.decrypt(algorithm, encryptedValue, cryptographerConfiguration.secret(), cryptographerConfiguration.keyLength(), cryptographerConfiguration.iterationCount());
  }

  @Override
  public SecretKey generateKey(byte[] salt) {
    return CryptoFunctions.generateKey(cryptographerConfiguration.secret(), salt, cryptographerConfiguration.keyLength(), cryptographerConfiguration.iterationCount());
  }

}
