package com.compilit.functions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * These functions all transform possible throwing operations into a definite result without throwing any exception.
 */
public final class FunctionResultGuards {

  private FunctionResultGuards() {}

  /**
   * @param supplier a potentially throwing supplier
   * @param <T>      the return type
   * @return either the result of the supplier or null
   */
  public static <T> T orNull(Supplier<T> supplier) {
    return orDefault(supplier, null);
  }

  /**
   * @param throwingSupplier a potentially checked exception throwing supplier
   * @param <T>              the return type
   * @param <E>              the checked exception
   * @return either the wanted value as a String or the given default value
   */
  public static <T, E extends Exception> T orNull(ThrowingSupplier<T, E> throwingSupplier) {
    return orDefault(throwingSupplier, null);
  }

  /**
   * @param function a potentially throwing supplier
   * @param <I>      the input type
   * @param <O>      the return type
   * @return either the result of the supplier value or null
   */
  public static <I, O> O orNull(Function<I, O> function, I value) {
    return orDefault(function, value, null);
  }

  /**
   * @param function a potentially throwing supplier
   * @param <I>      the input type
   * @param <O>      the return type
   * @return either the result of the supplier value or null
   */
  public static <I, O, E extends Exception> O orNull(
    ThrowingFunction<I, O, E> function,
    I value
  ) {
    return orDefault(function, value, null);
  }


  /**
   * @param supplier     a potentially throwing supplier
   * @param defaultValue the default value to return in case of an exception
   * @param <T>          the return type
   * @return either the result of the supplier or the given default value
   */
  public static <T> T orDefault(
    Supplier<T> supplier,
    T defaultValue
  ) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * @param supplier     a potentially throwing supplier
   * @param defaultValue the default value to return in case of an exception
   * @param <T>          the return type
   * @return either the result of the supplier or the given default value
   */
  public static <T, E extends Exception> T orDefault(
    ThrowingSupplier<T, E> supplier,
    T defaultValue
  ) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * @param function a potentially throwing function
   * @param <O>      the return type
   * @return either result of the function or the given default value
   */
  public static <I, O, E extends Exception> O orDefault(
    ThrowingFunction<I, O, E> function,
    I value,
    O defaultValue
  ) {
    try {
      return function.apply(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }


  /**
   * @param function a potentially throwing function
   * @param <O>      the return type
   * @return either result of the function or the given default value
   */
  public static <I, O> O orDefault(
    Function<I, O> function,
    I value,
    O defaultValue
  ) {
    try {
      return function.apply(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * Run the runnable without throwing an exception Wraps around checked exception-throwing functions
   *
   * @param runnable         the potentially throwing runnable
   * @param exceptionHandler the handler to handle the potential exception
   */
  public static void orHandleException(Runnable runnable, Consumer<Exception> exceptionHandler) {

    try {
      runnable.run();
    } catch (Exception e) {
      exceptionHandler.accept(e);
    }

  }

  /**
   * Run the runnable without throwing an exception Wraps around checked exception-throwing functions
   *
   * @param throwingRunnable the potentially throwing runnable
   * @param exceptionHandler the handler to handle the potential exception
   * @param <E>              the specific checked exception type
   */
  public static <E extends Exception> void orHandleException(
    ThrowingRunnable<E> throwingRunnable,
    Consumer<Exception> exceptionHandler
  ) {
    try {
      throwingRunnable.run();
    } catch (Exception e) {
      exceptionHandler.accept(e);
    }
  }

}
