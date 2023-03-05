package com.compilit.cryptography.api;

public enum KeyLength {
  BITS_128(128),
  BITS_192(192),
  BITS_256(256);

  private final int value;

  KeyLength(int value) {this.value = value;}

  public int getValue() {
    return value;
  }
}
