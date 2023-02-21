# Bali cryptography

With this package you can easily apply encryption and decryption of values using AES.

# Installation

Get this dependency with the latest version

```xml
    <dependency>
      <artifactId>cryptography-core</artifactId>
      <groupId>com.compilit</groupId>
    </dependency>
```

# Usage

If you don't have any special requirements as far as encryption key-length and iteration count, the easiest way to get
started is just to add an import to your Spring configuration:

```java
import org.springframework.context.annotation.Configuration;

@Import(DefaultCryptographyConfiguration.class)
@Configuration
class Example {
  (...)
}
```

Otherwise you could write an implementation for the Cryptographer interface.

The API speaks for itself, granted that you have done some research into AES encryption and decryption.