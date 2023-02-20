# results

Simple library to encapsulate and propagate processing results. Inspired by Results in the Rust
programming
language. (https://doc.rust-lang.org/std/result/enum.Result.html)

Often when something deep in our code goes wrong, we have only our exceptions to rely on propagating
error messages. But
what if what happens isn't an actual "exception"? Exceptions should be just that. Exceptional. For
everything else a
simple Result will suffice. Using results also enables you to better avoid using exceptions as a
control flow
mechanism (which is an anti-pattern).

Don't confuse server responses with Results. A 404 response can be wrapped in a "not found" result,
but a "not found"
result does not necessarily mean that somewhere a server gave you a 404 server response. This means
that it not always
straightforward to map a Result to a server response, be cautious.

# installation

Get this dependency with the latest version

```xml

<dependency>
  <artifactId>results</artifactId>
  <groupId>com.compilit</groupId>
</dependency>
```

# usage

Everything can be handled through the Result interface. Whenever you have some process that could
possibly fail, make
sure that it returns a Result. Which Result should be returned can be chosen manually or by passing
the process as a
function into the resultOf methods.

```java
import com.compilit.results.Result;

class Example {

  Result<?> exampleMethod1() {
    if (everythingWentWellInAVoidProcess()) {
      return Result.success();
    } else {
      return Result.errorOccurred(TEST_MESSAGE);
    }
  }

  Result<?> exampleMethod2() {
    if (everythingWentWellInAProcess()) {
      return Result.success(content);
    } else {
      return Result.errorOccurred(TEST_MESSAGE);
    }
  }

  Result<?> exampleMethod3() {
    if (something.doesNotMeetOurExpectations()) {
      return Result.unprocessable("Reason");
    } else {
      return Result.errorOccurred(TEST_MESSAGE);
    }
  }

  Result<?> exampleMethod4() {
    return Result.resultOf(() -> doSomethingDangerous());
  }

  Result<?> exampleMethod5() {
    return Result.<SomeOtherType>transform(Result.<OneType>errorOccured()); // Returns the error result, but without content.
  }


  Result<?> exampleMethod6() {
    return Result.transform(
      Result.errorOccured(),
      "some other content"
    ); // Returns the error result, but with different content.
  }

  Result<?> exampleMethod7() {
    return Result.combine(result1)
                 .with(result2)
                 .with(result3)
                 .merge(); // Returns a Result<List<T>> containing all contents. But only if all results were successful.
  }

  Result<?> exampleMethod8() {
    return Result.combine(result1)
                 .with(result2)
                 .with(result3)
                 .sum(); // Returns a SuccessResult. But only if all results were successful.
  }
}

```

### Chaining results

The OnSuccessMap method enables you to take your result and apply a function to it. But only in case
it is successful. This means you can chain result methods through a fluent API. Here is an example:

```java
class ExampleClass {
  (...)

  Result<String> getResult(Long id) {
    return respository.findById(id)
                      .onSuccessMap(entity -> entity.getName());
  }
}
```

Here we call some repository and transform the result into a String. If the result was not
successful the original result will be returned without content.

### Dealing with nested Results

The onSuccessMap method returns a Result as well, which means you can get into a situation where you
have a nested Result, like a Result<Result<String>>. There are numerous ways to deal with this.
Personally I'd advise the following approach:

```java
import com.compilit.results.Result;

class ExampleClass {
  (...)

  Result<String> getResult(Long id) {
    return Result.resultOf(() -> {
      var entity1 = respository.findById(id).getContentsOrElseThrow(); //would otherwise return a Result
      var entity2 = respository.findById(id).getContentsOrElseThrow(); //would otherwise return a Result
      return entity1.getName() + entity2.getName();
    });
  }
}
```

Since the orElseThrow method will take the result and transform it into the corresponding exception,
the resultOf function will in turn transform the result into the correct one complete with message.

In the same way, you can manually throw the respective NotFoundException, UnauthorizedException,
UnprocessableException and ErrorOccurredException within the scope of resultOf to get the desired
result.