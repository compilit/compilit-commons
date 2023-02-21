# Cryptographer Spring

A small extension on the Cryptographer library to easily register a CryptographerService.

Just annotate any Spring configuration file with the @EnableCryptography annotation and don't forget to register a
CryptographerConfiguration bean. This will be used to configure the CryptographerService.

# Installation

Get this dependency with the latest version

```xml

<dependency>
  <artifactId>cryptography-spring</artifactId>
  <groupId>com.compilit</groupId>
</dependency>
```

# Usage

Whenever an encrypted value is received that can and must be decrypted, annotate the parameter with @Decrypt and the
method with @Decryptable. In the same way annotate any argument that needs to be encrypted with @Encrypt and the method
with @Encryptable.

If Aspects aren't your thing you can just inject the registered Cryptographer directly in any class.