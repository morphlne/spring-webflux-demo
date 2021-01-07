package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTests {

  @Test
  public void withoutVirtualTime_thenSuccess() {
    Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
        .take(3)
        .log();

    StepVerifier.create(longFlux)
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }

  @Test
  public void withVirtualTime_thenSuccess() {

    VirtualTimeScheduler.getOrSet();

    Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
        .take(3)
        .log();

    StepVerifier.withVirtualTime(() -> longFlux)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(3))
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }

  @Test
  public void whenConcatWithVirtualTime_thenSuccess() {

    VirtualTimeScheduler.getOrSet();

    Flux<String> concated = Flux.concat(
        Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1)),
        Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))
    ).log();

    StepVerifier.withVirtualTime(() -> concated)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(6))
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }
}
