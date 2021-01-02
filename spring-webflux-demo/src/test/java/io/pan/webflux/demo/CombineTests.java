package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class CombineTests {

  @Test
  public void whenMerge_thenSuccess() {
    Flux<String> merged = Flux.merge(
        Flux.just("A", "B", "C"),
        Flux.just("D", "E", "F")
    );

    StepVerifier.create(merged)
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void whenMergeWithDelay_thenUnordered() {
    Flux<String> merged = Flux.merge(
        Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1)),
        Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))
    );

    StepVerifier.create(merged.log())
        .expectSubscription()
        .expectNextCount(6) //unordered due to execution in parallel
        .verifyComplete();
  }

  @Test
  public void whenConcatWithDelay_thenOrdered() {
    Flux<String> concated = Flux.concat(
        Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1)),
        Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))
    );

    StepVerifier.create(concated.log())
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  public void whenZip_thenSuccess() {
    Flux<String> zipped = Flux.zip(
        Flux.just("A", "B", "C"),
        Flux.just("D", "E", "F"),
        String::concat
    );

    StepVerifier.create(zipped.log())
        .expectSubscription()
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
  }
}
