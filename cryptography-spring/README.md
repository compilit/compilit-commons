# Cryptographer Spring

A small extension on the Cryptographer library to easily register a CryptographerService.

Just annotate any Spring configuration file with the @EnableCryptography annotation and don't forget to register a
custom CryptographerConfiguration bean. This will be used to configure the CryptographerService.

# Installation

Get this dependency with the latest version

```xml
    <dependency>
      <artifactId>cryptography-spring</artifactId>
      <groupId>com.compilit</groupId>
    </dependency>
```