package com.compilit.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This interface is your entrypoint for all result operations.
 *
 * @param <T> The type of the result you wish to return.
 */
public interface Result<T> {

  /**
   * A generic success result for a process or validation.
   *
   * @param <T> the type of the contents.
   * @return a success Result.
   */
  static <T> Result<T> success() {
    return new SuccessResult<>();
  }

  /**
   * A generic success result for a process or validation.
   *
   * @param contents the contents of the result.
   * @param <T>      the type of the contents.
   * @return a success Result with contents. Or an empty resource Result if the content is null.
   */
  static <T> Result<T> success(T contents) {
    return new SuccessResult<>(contents);
  }

  /**
   * A generic result for when the client asks for a non-existent resource.
   *
   * @param <T> the content type.
   * @return a not found Result without a message.
   */
  static <T> Result<T> notFound() {
    return new NotFoundResult<>();
  }

  /**
   * A generic result for when the client asks for a non-existent resource.
   *
   * @param <T>             the content type.
   * @param message         the message you wish to propagate.
   * @param formatArguments the message arguments you with to replace the '%s' (for example) symbol
   *                        with.
   * @return a not found Result with a message.
   */
  static <T> Result<T> notFound(String message, String... formatArguments) {
    var actualMessage = MessageFormatter.formatMessage(message, (Object[]) formatArguments);
    return new NotFoundResult<>(actualMessage);
  }

  /**
   * A generic failure result. Can be used for pretty much any failed process or validation.
   *
   * @param <T> the type of the contents.
   * @return an unprocessable Result without a message.
   */
  static <T> Result<T> unprocessable() {
    return new UnprocessableResult<>();
  }

  /**
   * A generic failure result. Can be used for pretty much any failed process or validation.
   *
   * @param message         the message you wish to propagate.
   * @param formatArguments the message arguments you with to replace the '%s' (for example) symbol
   *                        with.
   * @param <T>             the type of the contents.
   * @return an unprocessable Result with a message.
   */
  static <T> Result<T> unprocessable(String message, String... formatArguments) {
    var actualMessage = MessageFormatter.formatMessage(message, (Object[]) formatArguments);
    return new UnprocessableResult<>(actualMessage);
  }

  /**
   * A generic result for any encountered authentication/authorization issue.
   *
   * @param <T> the content type.
   * @return an empty unauthorized Result without a message.
   */
  static <T> Result<T> unauthorized() {
    return new UnauthorizedResult<>();
  }

  /**
   * A generic result for any encountered authentication/authorization issue.
   *
   * @param message         the message you wish to propagate.
   * @param formatArguments the message arguments you with to replace the '%s' (for example) symbol
   *                        with.
   * @param <T>             the content type.
   * @return an empty unauthorized Result with a message.
   */
  static <T> Result<T> unauthorized(String message, String... formatArguments) {
    var actualMessage = MessageFormatter.formatMessage(message, (Object[]) formatArguments);
    return new UnauthorizedResult<>(actualMessage);
  }

  /**
   * A generic result for any encountered exceptions.
   *
   * @param <T>             the content type.
   * @param message         the error message.
   * @param formatArguments the message arguments you with to replace the '%s' (for example) symbol
   *                        with.
   * @return an error occurred Result with a message.
   */
  static <T> Result<T> errorOccurred(String message, String... formatArguments) {
    var actualMessage = MessageFormatter.formatMessage(message, (Object[]) formatArguments);
    return new ErrorOccurredResult<>(actualMessage);
  }

  /**
   * A generic result that encapsulates a runnable process. Returns a Success result if the runnable
   * does not throw an Exception. If the Runnable throws an Exception, it returns a corresponding
   * result with the exception message added to the Result.
   *
   * @param runnable the actual process.
   * @param <T>      the content type.
   * @return SuccessResult or ErrorOccurredResult with the exception message.
   */
  static <T> Result<T> resultOf(Runnable runnable) {
    return resultOf(() -> {
      runnable.run();
      return null;
    });
  }

  /**
   * A generic result that encapsulates a supplying process. Returns a Success result with the
   * supplied content if the Supplier does not throw an Exception. If the Supplier throws an
   * Exception, it returns a corresponding result with the exception message added to the Result.
   *
   * @param supplier the content-supplying function.
   * @param <T>      the type of the contents.
   * @return SuccessResult or ErrorOccurredResult with the exception message.
   */
  static <T> Result<T> resultOf(Supplier<T> supplier) {
    try {
      var result = supplier.get();
      return Result.success(result);
    } catch (NotFoundException notFoundException) {
      return Result.notFound(notFoundException.getMessage());
    } catch (UnauthorizedException unauthorizedException) {
      return Result.unauthorized(unauthorizedException.getMessage());
    } catch (UnprocessableException unprocessableException) {
      return Result.unprocessable(unprocessableException.getMessage());
    } catch (Throwable defaultException) {
      return Result.errorOccurred(defaultException.getMessage());
    }
  }

  /**
   * A generic result that encapsulates a predicate. Returns a Success result with the value if the
   * predicate resolves to true. And an Unprocessable result if it resolves to false. If the
   * Predicate throws an Exception, it returns an ErrorOccurredResult with the exception message
   * added to the Result.
   *
   * @param predicate the predicate which to apply to the value.
   * @param value     the value which needs to be tested by the predicate.
   * @param <T>       the type of the contents.
   * @return SuccessResult, UnprocessableResult or ErrorOccurredResult with the exception message.
   */
  static <T> Result<T> resultOf(Predicate<T> predicate, T value) {
    try {
      var result = predicate.test(value);
      if (result) {
        return Result.success(value);
      }
      return Result.unprocessable();
    } catch (Exception exception) {
      return Result.errorOccurred(exception.getMessage());
    }
  }

  /**
   * Transforms an existing Result into another one while retaining the status. Works as an adapter.
   * Content will be lost and therefor this function should only be used to transform an already
   * failed result into something the client can use. Example: pass an Integer Result, but return a
   * String Result.
   *
   * @param result the existing Result.
   * @param <T>    the content type of the new Result.
   * @return Result.
   */
  static <T> Result<T> transform(Result<?> result) {
    return transform(result, null);
  }

  /**
   * Transforms an existing Result into another one while retaining the status. Works as an adapter.
   * Default content needs to be passed in order to provide the new Result with contents. Example:
   * pass an Integer Result, but return a String Result.
   *
   * @param result the existing Result.
   * @param <T>    the content type of the new Result.
   * @param value  the default content you wish to add to the final Result.
   * @return Result.
   */
  static <T> Result<T> transform(Result<?> result, T value) {
    var resultStatus = result.getResultStatus();
    var resultMessage = result.getMessage();
    return resultOf(resultStatus, resultMessage, value);
  }

  /**
   * Transforms an existing Result into another one with a different content while retaining the
   * status. Works as an adapter. Example: pass an Integer Result, but return a String Result.
   *
   * @param message         the message you with to add.
   * @param formatArguments the message arguments you with to replace the '%s' (for example) symbol
   *                        with.
   * @param result          the existing Result.
   * @param <T>             the content type of the new result.
   * @return Result.
   */
  static <T> Result<T> transform(Result<?> result, String message, String... formatArguments) {
    var resultStatus = result.getResultStatus();
    var actualMessage = MessageFormatter.formatMessage(message, (Object[]) formatArguments);
    return resultOf(resultStatus, actualMessage);
  }

  /**
   * @param <T>    the content type of the result.
   * @param result the result you wish to combine/merge/sum with others.
   * @return ResultCombiner to chain the next result.
   */
  static <T> ResultCombiner<T> combine(Result<T> result) {
    return new ResultToListCombiner<>(result);
  }

  /**
   * @param <T>     the content type of the result.
   * @param result  the result you wish to sum with others.
   * @param results the additional results you wish to sum with others.
   * @return the result sum of all results.
   */
  @SafeVarargs
  static <T> Result<T> sum(Result<T> result, Result<T>... results) {
    var resultList = new ArrayList<Result<T>>();
    if (results != null && results.length > 0) {
      resultList.addAll(Arrays.asList(results));
    }
    var resultCombiner = Result.combine(Result.<T>success()).with(result);
    for (var r : resultList) {
      resultCombiner.and(r);
    }
    return resultCombiner.sum();
  }

  private static <T> Result<T> resultOf(ResultStatus resultStatus, String message) {
    return resultOf(resultStatus, message, null);
  }

  private static <T> Result<T> resultOf(ResultStatus resultStatus, String message, T content) {
    switch (resultStatus) {
      case SUCCESS:
        return Result.success(content);
      case UNAUTHORIZED:
        return Result.unauthorized(message);
      case NOT_FOUND:
        return Result.notFound(message);
      case ERROR_OCCURRED:
        return Result.errorOccurred(message);
      default:
        return Result.unprocessable(message);
    }
  }

  /**
   * @return the ResultStatus of the result.
   */
  ResultStatus getResultStatus();

  /**
   * @return true if the ResultStatus equals SUCCESS or EMPTY_RESOURCE.
   */
  default boolean isSuccessful() {
    return getResultStatus().equals(ResultStatus.SUCCESS);
  }

  /**
   * @return true if the ResultStatus equals SUCCESS and has contents
   */
  default boolean isSuccessfulWithContents() {
    return isSuccessful() && hasContents();
  }

  /**
   * @return true if the ResultStatus does not equal SUCCESS.
   */
  default boolean isUnsuccessful() {
    return !isSuccessful();
  }

  /**
   * @return the nullable contents of the result.
   */
  default T getNullableContents() {
    return getContents().orElse(null);
  }

  /**
   * throw a FailedResultException with the result message if the result was unsuccessful.
   *
   * @return a successful result
   */
  default Result<T> orElseThrow() {
    return orElseThrow(resolveExceptionSupplier());
  }

  /**
   * throw the specified exception with the result message if the result was unsuccessful.
   *
   * @return a successful result
   */
  default <E extends RuntimeException> Result<T> orElseThrow(Supplier<E> exceptionSupplier) {
      if (isUnsuccessful()) {
          throw exceptionSupplier.get();
      }
    return this;
  }

  /**
   * throw an UnauthorizedException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return a successful result
   */
  default Result<T> orElseThrowUnauthorizedException() {
    return orElseThrow(() -> new UnauthorizedException(getMessage()));
  }

  /**
   * throw an NotFoundException with the result message if the result was unsuccessful. Otherwise,
   * return the result.
   *
   * @return a successful result
   */
  default Result<T> orElseThrowNotFoundException() {
    return orElseThrow(() -> new NotFoundException(getMessage()));
  }

  /**
   * throw an UnprocessableException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return a successful result
   */
  default Result<T> orElseThrowUnprocessableException() {
    return orElseThrow(() -> new UnprocessableException(getMessage()));
  }

  /**
   * throw an ErrorOccurredException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return a successful result
   */
  default Result<T> orElseThrowErrorOccurredException() {
    return orElseThrow(() -> new ErrorOccurredException(getMessage()));
  }

  /**
   * throw an UnauthorizedException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return the result contents
   */
  default T getContentsOrElseThrowUnauthorizedException() {
    return getContentsOrElseThrow(() -> new UnauthorizedException(getMessage()));
  }

  /**
   * throw an NotFoundException with the result message if the result was unsuccessful. Otherwise,
   * return the result.
   *
   * @return the result contents
   */
  default T getContentsOrElseThrowNotFoundException() {
    return getContentsOrElseThrow(() -> new NotFoundException(getMessage()));
  }

  /**
   * throw an UnprocessableException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return the result contents
   */
  default T getContentsOrElseThrowUnprocessableException() {
    return getContentsOrElseThrow(() -> new UnprocessableException(getMessage()));
  }

  /**
   * throw an ErrorOccurredException with the result message if the result was unsuccessful.
   * Otherwise, return the result.
   *
   * @return the result contents
   */
  default T getContentsOrElseThrowErrorOccurredException() {
    return getContentsOrElseThrow(() -> new ErrorOccurredException(getMessage()));
  }

  /**
   * @return the contents of the result or throw a NoContentsException with the result message.
   */
  default <E extends RuntimeException> T getContentsOrElseThrow() {
    return getContents().orElseThrow(resolveExceptionSupplier());
  }

  /**
   * @param <E>               any kind of RuntimeException you wish to throw if the contents are
   *                          null.
   * @param exceptionSupplier the supplier which you can use to define the desired exception in case
   *                          of an unsuccessful result.
   * @return the contents of the result or throw the given RuntimeException.
   */
  default <E extends RuntimeException> T getContentsOrElseThrow(Supplier<E> exceptionSupplier) {
    return getContents().orElseThrow(exceptionSupplier);
  }

  private Supplier<RuntimeException> resolveExceptionSupplier() {
    Supplier<RuntimeException> exceptionSupplier = RuntimeException::new;
    switch (getResultStatus()) {
      case UNPROCESSABLE:
        exceptionSupplier = () -> new UnprocessableException(getMessage());
        break;
      case UNAUTHORIZED:
        exceptionSupplier = () -> new UnauthorizedException(getMessage());
        break;
      case NOT_FOUND:
        exceptionSupplier = () -> new NotFoundException(getMessage());
        break;
      case ERROR_OCCURRED:
        exceptionSupplier = () -> new ErrorOccurredException(getMessage());
        break;
    }
    return exceptionSupplier;
  }

  /**
   * @return the optional contents of the result.
   */
  Optional<T> getContents();

  /**
   * @return the message of the result if present.
   */
  String getMessage();

  /**
   * @return true if the Result has contents.
   */
  default boolean hasContents() {
    return getContents().isPresent();
  }

  /**
   * @return true if the Result has no contents.
   */
  default boolean isEmpty() {
    return getContents().isEmpty();
  }

  /**
   * @param mappingFunction, the operation you wish to apply to the contents
   * @param <R>,             the return type
   * @return the contents of the result by applying a mapping function.
   */
  default <R> R getContents(Function<Optional<T>, R> mappingFunction) {
    return mappingFunction.apply(getContents());
  }

  /**
   * @param runnable, the operation you wish to perform on a success result
   * @return AlternativeRoute to specify a Runnable for any other situation
   */
  default Result<T> onSuccessRun(Runnable runnable) {
    if (isSuccessful()) {
      runnable.run();
    }
    return this;
  }

  /**
   * In case of a successful result, apply the mapping function to the content to return a different
   * content type. It should be noted that the result status "SUCCESS" does not automatically mean
   * the result has contents. Which is why the result of the onSuccessMap function can be influenced
   * by the mapping function that is passed.
   *
   * @param mappingFunction, the operation you wish to apply to the contents
   * @param <R>,             the return type
   * @return the final Result
   */
  default <R> Result<R> onSuccessMap(Function<T, R> mappingFunction) {
    if (isSuccessful()) {
      return Result.resultOf(() -> mappingFunction.apply(getContents().get()));
    }
    return Result.transform(this);
  }

    /**
     * In case of a successful result, apply the mapping function to the content to return a different
     * content type. Otherwise, return the result of the supplier. It should be noted that the result status "SUCCESS" does not automatically mean
     * the result has contents. Which is why the result of the onSuccessMap function can be influenced
     * by the mapping function that is passed.
     *
     * @param mappingFunction, the operation you wish to apply to the contents
     * @param supplier, the default return value in the form of a supplier
     * @param <R>,             the return type
     * @return the final Result
     */
    default <R> Result<R> onSuccessMapOrElse(Function<T, R> mappingFunction, Supplier<R> supplier) {
        if (isSuccessful()) {
            return Result.resultOf(() -> mappingFunction.apply(getContents().get()));
        }
        return Result.resultOf(supplier);
    }

  /**
   * A convenience method to transform the contents of your result into the desired return type.
   *
   * @param mappingFunction, the operation you wish to apply to the given result
   * @param <R>,             the return type
   * @return the result of the function
   */
  default <R> R transform(Function<Result<T>, R> mappingFunction) {
    return mappingFunction.apply(this);
  }


  /**
   * If the result was unsuccessful, ignore the actual status and convert it.
   *
   * @return an unauthorized result
   */
  default Result<T> orElseUnauthorized() {
    return orElse(Result.unauthorized(getMessage()));
  }

  /**
   * If the result was unsuccessful, ignore the actual status and convert it.
   *
   * @return a not found result
   */
  default Result<T> orElseNotFound() {
    return orElse(Result.notFound(getMessage()));
  }

  /**
   * If the result was unsuccessful, ignore the actual status and convert it.
   *
   * @return an unprocessable result
   */
  default Result<T> orElseUnprocessable() {
    return orElse(Result.unprocessable(getMessage()));
  }

  /**
   * If the result was unsuccessful, ignore the actual status and convert it.
   *
   * @return an error occurred result
   */
  default Result<T> orElseErrorOccurred() {
    return orElse(Result.errorOccurred(getMessage()));
  }

  /**
   * @param onUnsuccessfulResult the default result to return
   * @return the desired default in case of an unsuccessful result
   */
  default Result<T> orElse(Result<T> onUnsuccessfulResult) {
    return orElse(() -> onUnsuccessfulResult);
  }

  /**
   * @param supplier the default result supplier to return
   * @return the desired default in case of an unsuccessful result in the form of a Supplier
   */
  default Result<T> orElse(Supplier<Result<T>> supplier) {
      if (isUnsuccessful()) {
          return supplier.get();
      }
    return this;
  }

  /**
   * If the result was successful and has contents, performs the given consumer on the contents,
   * otherwise does nothing.
   *
   * @param consumer the action to be performed, if the result was successful and has contents
   */
  default void onSuccess(Consumer<? super T> consumer) {
    if (isSuccessfulWithContents()) {
      consumer.accept(getContents().get());
    }
  }

  /**
   * If the result was successful and has contents, performs the given consumer on the contents,
   * otherwise performs the given empty-based action.
   *
   * @param action      the action to be performed, if a value is present
   * @param emptyAction the empty-based action to be performed, if no value is present
   */
  default void onSuccessOrElse(Consumer<? super T> action, Runnable emptyAction) {
    if (isSuccessfulWithContents()) {
      action.accept(getContents().get());
    } else {
      emptyAction.run();
    }
  }

}
