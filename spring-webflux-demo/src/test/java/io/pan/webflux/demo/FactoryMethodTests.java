package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FactoryMethodTests {

  private final String[] colors;
  private final List<String> colorList;

  public FactoryMethodTests() {
    this.colors = new String[]{"red", "green", "blue"};
    this.colorList = Arrays.asList(colors);
  }

  @Test
  public void fluxFromArray() {
    StepVerifier.create(
        Flux.fromArray(colors)
    ).expectNextCount(3)
        .verifyComplete();
  }

  @Test
  public void fluxFromList() {
    StepVerifier.create(
        Flux.fromIterable(colorList)
    ).expectNextCount(3)
        .verifyComplete();
  }

  @Test
  public void fluxFromStream() {
    StepVerifier.create(
        Flux.fromStream(colorList.stream())
    ).expectNextCount(3)
        .verifyComplete();
  }

  @Test
  public void fluxFromRange() {
    StepVerifier.create(
        Flux.range(1, 3)
    ).expectNext(1, 2, 3)
        .verifyComplete();
  }

  @Test
  public void emptyMono() {
    StepVerifier.create(
        Mono.justOrEmpty(null)
    ).verifyComplete();
  }

  @Test
  public void monoFromSupplier() {
    StepVerifier.create(
        Mono.fromSupplier(() -> "red")
    ).expectNext("red")
        .verifyComplete();
  }

  @Test
  public void monoFromFuture() {
    StepVerifier.create(
        Mono.fromFuture(
            CompletableFuture.supplyAsync(() -> "red")
        )
    ).expectNext("red")
        .verifyComplete();
  }

  @Test
  public void monoFromFuture_thenError() {
    StepVerifier.create(
        Mono.fromFuture(
            CompletableFuture.supplyAsync(() -> {
              throw new RuntimeException();
            })
        )
    ).expectError(RuntimeException.class)
        .verify();
  }
}
