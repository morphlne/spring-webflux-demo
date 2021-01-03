package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class InfiniteTests {

  @Test
  public void whenInfiniteStream_thenSuccess() throws InterruptedException {
    Flux.interval(Duration.ofMillis(200))
        .log()
        .subscribe(System.out::println);

    Thread.sleep(2000);
  }

  @Test
  public void whenLimitedStream_thenSuccess() {
    Flux<Long> limited = Flux.interval(Duration.ofMillis(200))
        .take(3)
        .log();

    StepVerifier.create(limited)
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }

  @Test
  public void whenMapStream_thenSuccess() {
    Flux<Integer> limited = Flux.interval(Duration.ofMillis(200))
        .map(Long::intValue)
        .take(3)
        .log();

    StepVerifier.create(limited)
        .expectSubscription()
        .expectNext(0, 1, 2)
        .verifyComplete();
  }

  @Test
  public void whenDelay_thenSuccess() {
    Flux<Long> limited = Flux.interval(Duration.ofMillis(200))
        .delayElements(Duration.ofSeconds(1))
        .take(3)
        .log();

    StepVerifier.create(limited)
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }
}
