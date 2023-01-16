package com.compilit.core.api.cryptography;

import com.compilit.core.api.security.User;
import javax.crypto.SecretKey;

/**
 * This entire interface is built with AES encryption in mind. Where the user can present a secret used for encryption.
 */
public interface Cryptographer {

    /**
     * Encrypt the given value based on the presented users secret
     * @param valueToEncrypt the value you wish to encrypt
     * @param user the user whose secret will be used for encryption
     * @return the encrypted value in the form of a String
     */
    String encrypt(String valueToEncrypt, User user);

    /**
     * Encrypt the given value based on the presented users secret
     * @param valueToEncrypt the value you wish to encrypt
     * @param user the user whose secret will be used for encryption
     * @return the encrypted value in the form of a byte array
     */
    byte[] encrypt(byte[] valueToEncrypt, User user);

    /**
     * Encrypt the given value based on the presented users secret
     * @param valueToEncrypt the value you wish to encrypt
     * @param secret the secret used for encryption
     * @param salt the salt you wish to use for the encryption instead of the default
     * @return the encrypted value in the form of a String
     */
    byte[] encrypt(byte[] valueToEncrypt, String secret, byte[] salt);

    /**
     * Encrypt the given value based on the presented users secret
     * @param algorithm the algorithm you wish to use instead of the default
     * @param valueToEncrypt the value you wish to encrypt
     * @param salt the salt you wish to use for the encryption
     * @param key the salt you wish to use for the encryption
     * @return the encrypted value in the form of a String
     */
    byte[] encrypt(
        String algorithm,
        byte[] valueToEncrypt,
        byte[] salt,
        SecretKey key
    );

    /**
     * @param encryptedValue the value that will be decrypted
     * @param user the user whose secret will be used for the decryption
     * @return the decrypted value
     */
    String decrypt(
        String encryptedValue,
        User user
    );

    /**
     * @param encryptedValue the value that will be decrypted
     * @param user the user whose secret will be used for the decryption
     * @return the decrypted value
     */
    byte[] decrypt(
        byte[] encryptedValue,
        User user
    );

    /**
     * @param encryptedValue the value that will be decrypted
     * @param secret the users secret which will be used for the decryption
     * @return the decrypted value
     */
    byte[] decrypt(
        byte[] encryptedValue,
        String secret
    );

    /**
     * @param algorithm algorithm which needs to be used for the decryption instead of the default
     * @param encryptedValue the value that will be decrypted
     * @param secret the users secret which will be used for the decryption
     * @return the decrypted value
     */
    byte[] decrypt(
        String algorithm,
        byte[] encryptedValue,
        String secret
    );
}
