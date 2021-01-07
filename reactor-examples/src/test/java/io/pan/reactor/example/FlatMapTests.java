package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FlatMapTests {

  private final FluxColor colors;

  public FlatMapTests() {
    this.colors = new FluxColor();
  }

  @Test
  public void whenFlatMap_thenSuccess() {
    StepVerifier.create(
        colors.all().flatMap(this::toFlux)
    ).expectNextCount(6)
        .verifyComplete();
  }

  @Test
  public void whenSlowFlatMap_thenSuccess() {
    StepVerifier.create(
        Flux.just(1, 2, 3, 4, 5, 6)
            .map(this::slowDbRequest)
            .flatMap(Flux::fromIterable)
            .log()
    ).expectNextCount(12)
        .verifyComplete();
  }

  @Test
  public void whenParallelFlatMap_thenSuccess() {
    StepVerifier.create(
        Flux.just(1, 2, 3, 4, 5, 6)
            .window(2)
            .flatMap(
                i -> i.map(this::slowDbRequest)
                    .subscribeOn(parallel())
            ).flatMap(Flux::fromIterable)
            .log()
    ).expectNextCount(12)
        .verifyComplete();
  }

  @Test
  public void whenParallelFlatMap_thenOrdered() {
    StepVerifier.create(
        Flux.just(1, 2, 3, 4, 5, 6)
            .window(2)
            .flatMapSequential(
                i -> i.map(this::slowDbRequest)
                    .subscribeOn(parallel())
            ).flatMap(Flux::fromIterable)
            .log()
    ).expectNextCount(12)
        .verifyComplete();
  }

  private Flux<String> toFlux(String s) {
    return Flux.just(s, "grey");
  }

  private List<Integer> slowDbRequest(Integer i) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    return Arrays.asList(i, 0);
  }
}
