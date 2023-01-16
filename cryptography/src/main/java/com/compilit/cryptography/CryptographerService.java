package com.compilit.cryptography;

import static com.compilit.cryptography.CryptographyDefaults.ENCRYPTION_ALGORITHM;

import com.compilit.core.api.cryptography.Cryptographer;
import com.compilit.core.api.security.User;
import jakarta.annotation.ManagedBean;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ManagedBean
class CryptographerService implements Cryptographer {
    private final int keyLength;
    private final int iterationCount;

    CryptographerService(int keyLength, int iterationCount) {
        this.keyLength = keyLength;
        this.iterationCount = iterationCount;
    }

    @Override
    public String encrypt(String valueToEncrypt, User user) {
        return new String(CryptoFunctions.encrypt(valueToEncrypt.getBytes(), user.getPassword(), CryptoFunctions.generateNonce()));
    }
    @Override
    public byte[] encrypt(byte[] valueToEncrypt, User user) {
        return CryptoFunctions.encrypt(valueToEncrypt, user.getPassword(), CryptoFunctions.generateNonce());
    }

    @Override
    public byte[] encrypt(byte[] valueToEncrypt, String password, byte[] salt) {
        return CryptoFunctions.encrypt(ENCRYPTION_ALGORITHM, valueToEncrypt, salt, CryptoFunctions.generateKey(password, salt));
    }

    @Override
    public byte[] encrypt(
        String algorithm,
        byte[] valueToEncrypt,
        byte[] salt,
        SecretKey key
    ) {
        return CryptoFunctions.encrypt(algorithm, valueToEncrypt, salt, key);
    }

    @Override
    public String decrypt(
        String encryptedValue,
        User user
    ) {
        return new String(CryptoFunctions.decrypt(encryptedValue.getBytes(), user.getPassword()));
    }
    @Override
    public byte[] decrypt(
        byte[] encryptedValue,
        User user
    ) {
        return CryptoFunctions.decrypt(encryptedValue, user.getPassword());
    }

    @Override
    public byte[] decrypt(
        byte[] encryptedValue,
        String password
    ) {
        return CryptoFunctions.decrypt(ENCRYPTION_ALGORITHM, encryptedValue, password);
    }

    @Override
    public byte[] decrypt(
        String algorithm,
        byte[] encryptedValue,
        String password
    ) {
        return CryptoFunctions.decrypt(algorithm, encryptedValue, password);
    }

}
