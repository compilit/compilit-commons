# Compilit cryptography

With this package you can easily apply encryption and decryption of values using AES.

### Usage
If you don't have any special requirements as far as encryption key-length and iteration count, the easiest way to get started is just to add an import to your Spring configuration:

```java
import org.springframework.context.annotation.Configuration;

@Import(DefaultCryptographyConfiguration.class)
@Configuration
class Example {
  (...)
}
```

The API speaks for itself, granted that you have done some research into AES encryption and decryption.