package com.compilit.cryptography;

import com.compilit.cryptography.api.KeyLength;

/**
 * @param keyLength      your preferred keyLength, 128, 192 or 256
 * @param iterationCount the desired amount of iterations for the algorithm. The more iterations, the harder it will be
 *                       to crack te encryption, but the slower the algorithm becomes. It is recommended to not go under
 *                       1000 iterations.
 */
public record CryptographerConfiguration(String secret, KeyLength keyLength, int iterationCount) {
}
