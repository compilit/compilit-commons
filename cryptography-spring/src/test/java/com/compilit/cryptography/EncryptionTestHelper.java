package com.compilit.cryptography;

import com.compilit.cryptography.api.Decrypt;
import com.compilit.cryptography.api.Decryptable;
import com.compilit.cryptography.api.Encrypt;
import com.compilit.cryptography.api.Encryptable;

public class EncryptionTestHelper {

  @Encryptable
  public Object encrypt(@Encrypt Object valueToEncrypt) {
    return valueToEncrypt;
  }

  @Decryptable
  public Object decrypt(@Decrypt Object valueToEncrypt) {
    return valueToEncrypt;
  }
}
