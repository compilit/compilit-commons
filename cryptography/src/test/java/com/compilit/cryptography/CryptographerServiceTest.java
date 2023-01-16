package com.compilit.cryptography;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.compilit.core.api.cryptography.Cryptographer;
import com.compilit.core.api.security.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CryptographerServiceTest {

    private final String password = RandomStringUtils.randomAscii(32);
    private final Cryptographer cryptographerService = new DefaultCryptographyConfiguration().cryptographer();
    private final User user = Mockito.mock(User.class);

    @Test
    void encryptAndDecrypt_shouldEncryptAndDecryptValue() {
        when(user.getPassword()).thenReturn(password);
        String valueToEncrypt = "IamSomeSortOfSecretApparently";
        String encryptedValue = cryptographerService.encrypt(valueToEncrypt, user);
        assertThat(valueToEncrypt).isNotEqualTo(encryptedValue);
        String decryptedValue = cryptographerService.decrypt(encryptedValue, user);
        assertThat(valueToEncrypt).isEqualTo(decryptedValue);
    }

}