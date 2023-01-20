package com.compilit.cryptography.api;

import static com.compilit.cryptography.api.CryptographyDefaults.ENCRYPTION_ALGORITHM;
import static com.compilit.cryptography.api.CryptographyDefaults.ITERATION_COUNT;
import static com.compilit.cryptography.api.CryptographyDefaults.IV_LENGTH;
import static com.compilit.cryptography.api.CryptographyDefaults.KEY_LENGTH;
import static com.compilit.cryptography.api.CryptographyDefaults.SALT_LENGTH;
import static com.compilit.cryptography.api.CryptographyDefaults.SECRET_KEY_FACTORY_ALGORITHM;
import static com.compilit.cryptography.api.CryptographyDefaults.SECRET_KEY_SPEC_ALGORITHM;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple class containing functions to encrypt and decrypt values with an intuitive API.
 *
 * @see Cryptographer
 */
public final class CryptoFunctions {

  private CryptoFunctions() {}

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param valueToEncrypt the value you wish to encrypt.
   * @param secret         the secret used for encryption.
   * @return the encrypted value in the form of a String.
   */
  public static <T extends Serializable> byte[] encrypt(T valueToEncrypt, String secret) {
    var salt = generateNonce();
    return encrypt(ENCRYPTION_ALGORITHM, serialize(valueToEncrypt), salt, generateKey(secret, salt));
  }

  /**
   * Encrypt the given value based on the presented users secret.
   *
   * @param valueToEncrypt the value you wish to encrypt.
   * @param secret         the secret used for encryption.
   * @param salt           the salt you wish to use for the encryption instead of the default.
   * @return the encrypted value in the form of a String.
   */
  public static <T extends Serializable> byte[] encrypt(T valueToEncrypt, String secret, byte[] salt) {
    return encrypt(ENCRYPTION_ALGORITHM, serialize(valueToEncrypt), salt, generateKey(secret, salt));
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
  public static byte[] encrypt(
    String algorithm,
    byte[] valueToEncrypt,
    byte[] salt,
    SecretKey key
  ) {
    try {
      var cipher = Cipher.getInstance(algorithm);
      var iv = generateIv();
      cipher.init(Cipher.ENCRYPT_MODE, key, iv);
      var encryptedValue = cipher.doFinal(valueToEncrypt);
      var encryptedValueWithIv = prependIv(encryptedValue, iv.getIV());
      var encryptedValueWithIvAndSalt = prependIv(encryptedValueWithIv, salt);
      return Base64.getEncoder()
                   .encodeToString(encryptedValueWithIvAndSalt).getBytes();
    } catch (Exception e) {
      throw new CryptographyException(e);
    }
  }

  /**
   * @param encryptedValue the value that will be decrypted.
   * @param secret         the users secret which will be used for the decryption.
   * @return the decrypted value.
   */
  public static <T extends Serializable> T decrypt(
    byte[] encryptedValue,
    String secret
  ) {
    return decrypt(ENCRYPTION_ALGORITHM, encryptedValue, secret);
  }

  /**
   * @param algorithm      algorithm which needs to be used for the decryption instead of the default.
   * @param encryptedValue the value that will be decrypted.
   * @param secret         the users secret which will be used for the decryption.
   * @return the decrypted value.
   */
  public static <T extends Serializable> T decrypt(
    String algorithm,
    byte[] encryptedValue,
    String secret
  ) {
    try {
      Cipher cipher = Cipher.getInstance(algorithm);
      var bytesToDecryptWithIvAndSalt = Base64.getDecoder().decode(encryptedValue);
      var salt = resolveSalt(bytesToDecryptWithIvAndSalt);
      var bytesToDecryptWithIv = stripTailBytes(bytesToDecryptWithIvAndSalt, SALT_LENGTH);
      var key = generateKey(secret, salt);
      var iv = resolveIv(bytesToDecryptWithIv);
      var bytesToDecryptWithoutIv = stripTailBytes(bytesToDecryptWithIv, IV_LENGTH);
      cipher.init(Cipher.DECRYPT_MODE, key, iv);
      var decryptedBytes = cipher.doFinal(bytesToDecryptWithoutIv);
      return deserialize(decryptedBytes);
    } catch (Exception e) {
      throw new CryptographyException(e);
    }
  }

  static IvParameterSpec generateIv() {
    byte[] iv = generateNonce();
    return new IvParameterSpec(iv);
  }

  static byte[] generateNonce() {
    byte[] iv = new byte[CryptographyDefaults.NONCE_LENGTH];
    new SecureRandom().nextBytes(iv);
    return iv;
  }

  static SecretKey generateKey(String secret, byte[] salt) {
    return generateKey(secret, salt, KEY_LENGTH, ITERATION_COUNT);
  }

  static SecretKey generateKey(String secret, byte[] salt, int keyLength, int iterationCount) {
    try {
      var factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
      var keySpec = new PBEKeySpec(
        secret.toCharArray(),
        salt,
        iterationCount,
        keyLength
      );
      var key = factory.generateSecret(keySpec).getEncoded();
      return new SecretKeySpec(key, SECRET_KEY_SPEC_ALGORITHM);
    } catch (Exception e) {
      throw new CryptographyException(e);
    }
  }

  private static byte[] prependIv(
    byte[] encryptedData,
    byte[] iv
  ) {
    var encryptedDataWithIv = new byte[encryptedData.length + IV_LENGTH];
    System.arraycopy(iv, 0, encryptedDataWithIv, 0, IV_LENGTH);
    System.arraycopy(encryptedData, 0, encryptedDataWithIv, IV_LENGTH, encryptedData.length);
    return encryptedDataWithIv;
  }

  private static IvParameterSpec resolveIv(byte[] encryptedData) {
    byte[] iv = new byte[IV_LENGTH];
    System.arraycopy(encryptedData, 0, iv, 0, iv.length);
    return new IvParameterSpec(iv);
  }

  private static byte[] resolveSalt(byte[] encryptedData) {
    byte[] salt = new byte[SALT_LENGTH];
    System.arraycopy(encryptedData, 0, salt, 0, salt.length);
    return salt;
  }

  private static byte[] stripTailBytes(byte[] encryptedData, int length) {
    byte[] encryptedDataWithoutBytes = new byte[encryptedData.length - length];
    System.arraycopy(
      encryptedData,
      length,
      encryptedDataWithoutBytes,
      0,
      encryptedData.length - length
    );
    return encryptedDataWithoutBytes;
  }

}