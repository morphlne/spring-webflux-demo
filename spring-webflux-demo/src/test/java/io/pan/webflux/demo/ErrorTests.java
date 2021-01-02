package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class ErrorTests {

  @Test
  public void whenExpectError_thenSuccess() {
    StepVerifier.create(new FluxColor().allWithError())
        .expectNext("red", "green", "orange")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void whenOnErrorResume_thenSuccess() {
    Flux<String> flux = new FluxColor().allWithError()
        .onErrorResume(e -> {
          e.printStackTrace();
          return Flux.just("default");
        });

    StepVerifier.create(flux)
        .expectNext("red", "green", "orange")
        .expectNext("default")
        .verifyComplete();
  }

  @Test
  public void whenOnErrorReturn_thenSuccess() {
    Flux<String> flux = new FluxColor().allWithError()
        .onErrorReturn("default");

    StepVerifier.create(flux)
        .expectNext("red", "green", "orange")
        .expectNext("default")
        .verifyComplete();
  }

  @Test
  public void whenOnErrorMap_thenSuccess() {
    Flux<String> flux = new FluxColor().allWithError()
        .onErrorMap(BusinessException::new);

    StepVerifier.create(flux)
        .expectNext("red", "green", "orange")
        .expectError(BusinessException.class)
        .verify();
  }

  @Test
  public void whenOnErrorMapWithRetry_thenSuccess() {
    Flux<String> flux = new FluxColor().allWithError()
        .onErrorMap(BusinessException::new)
        .retry(2);

    StepVerifier.create(flux.log())
        .expectNext(
            "red", "green", "orange",
            "red", "green", "orange",
            "red", "green", "orange"
        )
        .expectError(BusinessException.class)
        .verify();
  }

  @Test
  public void whenRetryBackoff_thenSuccess() {
    Flux<String> flux = new FluxColor().allWithError()
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(3)));

    StepVerifier.create(flux.log())
        .expectNext(
            "red", "green", "orange",
            "red", "green", "orange",
            "red", "green", "orange"
        )
        .expectError(IllegalStateException.class) //RetryExhaustedException extends IllegalStateException
        .verify();
  }

  public class BusinessException extends RuntimeException {
    public BusinessException(Throwable throwable) {
      super(throwable);
    }
  }
}
