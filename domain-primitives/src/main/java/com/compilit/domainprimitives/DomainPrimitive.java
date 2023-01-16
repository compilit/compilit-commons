package com.compilit.domainprimitives;

import static com.compilit.validation.api.Verifications.verifyThat;

import com.compilit.core.api.cryptography.Cryptographer;
import com.compilit.core.api.security.User;
import com.compilit.core.api.validation.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DomainPrimitive<T> {

  private final T value;
  private final String name;

  private final List<Rule<T>> rules = new ArrayList<>();

  /**
   * @param value the value of the domain primitive.
   * @param extendingClass the class that extends DomainPrimitive. Used to resolve the name.
   * @param rules the rules you wish to define for the given value
   */
  @SafeVarargs
  protected DomainPrimitive(T value, Class<?> extendingClass, Rule<T>... rules) {
    this(value, extendingClass.getSimpleName(), rules);
  }

  /**
   * @param value the value of the domain primitive
   * @param name the actual name of the domain primitive. Usually the name of the extending class.
   */
  @SafeVarargs
  protected DomainPrimitive(T value, String name, Rule<T>... rules) {
    this.name = name;
    this.value = value;
    if (rules != null) {
      this.rules.addAll(List.of(rules));
    }
    if (!isValid(value)) {
      throw new DomainPrimitiveException(getName(), value);
    }
  }

  @Override
  public boolean equals(Object obj) {
    try {
      if (obj instanceof DomainPrimitive) {
        var otherValue = ((DomainPrimitive<T>) obj).getValue();
        return Objects.equals(getValue(), otherValue);
      }
      return Objects.equals(this.value, obj);
    } catch (Exception exception) {
      return false;
    }
  }

  public T getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  public String getEncryptedValue(User user, Cryptographer cryptographer) {
    return cryptographer.encrypt(String.valueOf(value), user);
  }

  public String gerDecryptedValue(User user, Cryptographer cryptographer) {
    return cryptographer.decrypt(String.valueOf(value), user);
  }

  protected boolean isValid(T value) {
     validateByRules(value);
     return true;
  }

  protected void validateByRules(T value) {
    if (rules.isEmpty())
      throw new DomainPrimitiveException(getName(), value, "No rules were registered");
    verifyThat(getValue())
      .compliesWith(rules)
      .orElseThrow(message -> {
        throw new DomainPrimitiveException(getName(), value, message);
      });
  }
}
