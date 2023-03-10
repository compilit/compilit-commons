package com.compilit.domainprimitives;

import static com.compilit.validation.Verifications.verifyThat;

import com.compilit.validation.api.Rule;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A domain primitive adds specific constrains to a simple primitive. These constraints should be domain-specific.
 * Hence, the name.
 *
 * @param <T> The primitive type which this domain primitive wraps around
 */
public abstract class DomainPrimitive<T> {

  private final T value;
  private final String name;
  private final List<Rule<T>> rules = new ArrayList<>();


  /**
   * @param value          the value of the domain primitive.
   * @param extendingClass the class that extends DomainPrimitive. Used to resolve the name.
   * @param rules          the rules you wish to define for the given value
   */
  @SafeVarargs
  protected DomainPrimitive(T value, Class<?> extendingClass, Rule<T>... rules) {
    this(value, extendingClass.getSimpleName(), rules);
  }

  /**
   * @param value the value of the domain primitive
   * @param name  the actual name of the domain primitive. Usually the name of the extending class.
   * @param rules the rules you wish to define for the given value
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
      if (obj instanceof DomainPrimitive<?> domainPrimitive) {
        var otherValue = domainPrimitive.getValue();
        return Objects.equals(getValue(), otherValue);
      }
      return Objects.equals(this.value, obj);
    } catch (Exception exception) {
      return false;
    }
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  public T getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  protected boolean isValid(T value) {
    validateByRules(value);
    return true;
  }

  protected void validateByRules(T value) {
    if (rules.isEmpty()) {
      throw new DomainPrimitiveException(getName(), value, "No rules were registered");
    }
    verifyThat(getValue())
      .compliesWith(rules)
      .orElseThrow(message -> {
        throw new DomainPrimitiveException(getName(), value, message);
      });
  }
}
