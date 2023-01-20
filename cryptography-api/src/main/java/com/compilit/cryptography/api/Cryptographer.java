package com.compilit.cryptography.api;

import static com.compilit.cryptography.api.CryptographyDefaults.ITERATION_COUNT;
import static com.compilit.cryptography.api.CryptographyDefaults.KEY_LENGTH;
import static org.apache.commons.lang3.SerializationUtils.serialize;

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
   * @param secret         the secret which will be used for encryption.
   * @return the encrypted value in the form of a byte array.
   */
  default <T extends Serializable> byte[] encrypt(T valueToEncrypt, String secret) {
    return encrypt(valueToEncrypt, secret, generateNonce());
  }

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param valueToEncrypt the value you wish to encrypt.
   * @param secret         the user whose secret will be used for encryption.
   * @param salt           the salt you wish to use for the encryption instead of the default.
   * @return the encrypted value in the form of a String.
   */
  default <T extends Serializable> byte[] encrypt(T valueToEncrypt, String secret, byte[] salt) {
    return encrypt(getAlgorithm(), valueToEncrypt, salt, generateKey(secret, salt));
  }

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param algorithm      the algorithm you wish to use instead of the default.
   * @param valueToEncrypt the value you wish to encrypt.
   * @param salt           the salt you wish to use for the encryption.
   * @param key            the salt you wish to use for the encryption.
   * @return the encrypted value in the form of a String.
   */
  default <T extends Serializable> byte[] encrypt(
    String algorithm,
    T valueToEncrypt,
    byte[] salt,
    SecretKey key
  ) {
    return CryptoFunctions.encrypt(algorithm, serialize(valueToEncrypt), salt, key);
  }

  // From here on are the decryption methods

  /**
   * @param encryptedValue the value that will be decrypted.
   * @param secret         the secret which will be used for the decryption.
   * @return the decrypted value.
   */
  default <T extends Serializable> T decrypt(
    String encryptedValue,
    String secret
  ) {
    return decrypt(encryptedValue.getBytes(), secret);
  }

  /**
   * @param encryptedValue the value that will be decrypted.
   * @param secret         the secret which will be used for the decryption.
   * @return the decrypted value.
   */
  default <T extends Serializable> T decrypt(
    byte[] encryptedValue,
    String secret
  ) {
    return decrypt(getAlgorithm(), encryptedValue, secret);
  }

  /**
   * @param algorithm      algorithm which needs to be used for the decryption instead of the default.
   * @param encryptedValue the value that will be decrypted.
   * @param secret         the users secret which will be used for the decryption.
   * @return the decrypted value.
   */
  default <T extends Serializable> T decrypt(
    String algorithm,
    byte[] encryptedValue,
    String secret
  ) {
    return CryptoFunctions.decrypt(algorithm, encryptedValue, secret);
  }

  /**
   * @return a secure random byte array with a default length of 16.
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
   * Generate a key based on the given arguments. Defaulting the keyLength and iterationCount to 256 and 65536
   * respectively.
   *
   * @param secret the secret which will be used in combination with the salt to generate the key.
   * @param salt   the salt which will be used in combination with the secret to generate the key.
   * @return a SecretKey which can be used to encrypt data.
   */
  default SecretKey generateKey(String secret, byte[] salt) {
    return generateKey(secret, salt, KEY_LENGTH, ITERATION_COUNT);
  }

  /**
   * Generate a key based on the given arguments. Caution should be used when supplying your own keyLength. Since most
   * JDK's don't have the 'Unlimited Strength Jurisdiction Policy' enabled by default, keyLengths longer than 256 won't
   * work. If you wish to use longer keys, you'll need to enable the Unlimited Strength Jurisdiction Policy.
   *
   * @param secret         the secret which will be used in combination with the salt to generate the key.
   * @param salt           the salt which will be used in combination with the secret to generate the key.
   * @param keyLength      the length of the key, should not exceed 256 if the Unlimited Strength Jurisdiction Policy is
   *                       not enabled in your JDK.
   * @param iterationCount the number of times the password is hashed. The more iterations, the harder it will be for
   *                       attackers to get hold of this key.
   * @return a SecretKey which can be used to encrypt data.
   */
  default SecretKey generateKey(String secret, byte[] salt, int keyLength, int iterationCount) {
    return CryptoFunctions.generateKey(secret, salt, keyLength, iterationCount);
  }
}
