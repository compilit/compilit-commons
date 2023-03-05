package com.compilit.cryptography.api;

import java.io.Serializable;
import java.security.SecureRandom;
import javax.crypto.SecretKey;

/**
 * This entire interface is built with AES encryption in mind. Where the user can present a secret used for encryption.
 * This kind of encryption can be used to let the user be in control of its own data, since only the user can unlock the
 * data.
 */
public interface Cryptographer {

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param valueToEncrypt the value you wish to encrypt.
   * @return the encrypted value in the form of a byte array.
   */
  default <T extends Serializable> byte[] encrypt(T valueToEncrypt) {
    return encrypt(valueToEncrypt, generateNonce());
  }

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param valueToEncrypt the value you wish to encrypt.
   * @param salt           the salt you wish to use for the encryption instead of the
   * @return the encrypted value in the form of a String.
   */
  default <T extends Serializable> byte[] encrypt(T valueToEncrypt, byte[] salt) {
    return encrypt(getAlgorithm(), valueToEncrypt, salt, generateKey(salt));
  }

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param algorithm      the algorithm you wish to use instead of the
   * @param valueToEncrypt the value you wish to encrypt.
   * @param salt           the salt you wish to use for the encryption.
   * @param key            the salt you wish to use for the encryption.
   * @return the encrypted value in the form of a String.
   */
  <T extends Serializable> byte[] encrypt(
    String algorithm,
    T valueToEncrypt,
    byte[] salt,
    SecretKey key
  );

  // From here on are the decryption methods

  /**
   * @param encryptedValue the value that will be decrypted.
   * @return the decrypted value.
   */
  default <T extends Serializable> T decrypt(
    String encryptedValue
  ) {
    return decrypt(encryptedValue.getBytes());
  }

  /**
   * @param encryptedValue the value that will be decrypted.
   * @return the decrypted value.
   */
  default <T extends Serializable> T decrypt(
    byte[] encryptedValue
  ) {
    return decrypt(getAlgorithm(), encryptedValue);
  }

  /**
   * @param algorithm      algorithm which needs to be used for the decryption instead of the
   * @param encryptedValue the value that will be decrypted.
   * @return the decrypted value.
   */
  <T extends Serializable> T decrypt(
    String algorithm,
    byte[] encryptedValue
  );

  /**
   * @return a secure random byte array with a length of 16.
   */
  default byte[] generateNonce() {
    byte[] iv = new byte[CryptographyDefaults.NONCE_LENGTH];
    new SecureRandom().nextBytes(iv);
    return iv;
  }

  /**
   * @return the encryption algorithm you wish to use. Defaults to AES/CBC/PKCS5Padding.
   */
  default String getAlgorithm() {
    return CryptographyDefaults.ENCRYPTION_ALGORITHM;
  }

  /**
   * Generate a key based on the given arguments. Caution should be used when supplying your own keyLength. Since most
   * JDK's don't have the 'Unlimited Strength Jurisdiction Policy' enabled by  keyLengths longer than 256 won't work. If
   * you wish to use longer keys, you'll need to enable the Unlimited Strength Jurisdiction Policy.
   *
   * @param salt the salt which will be used in combination with the secret to generate the key.
   * @return a SecretKey which can be used to encrypt data.
   */
  SecretKey generateKey(byte[] salt);
}
