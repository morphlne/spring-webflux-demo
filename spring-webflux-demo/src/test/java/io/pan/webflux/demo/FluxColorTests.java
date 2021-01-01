package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxColorTests {

  private final FluxColor color;

  public FluxColorTests() {
    this.color = new FluxColor();
  }

  @Test
  void getColors_withoutErrors() {
    StepVerifier.create(color.all())
        .expectNext("red")
        .expectNext("green")
        .expectNext("orange")
        .verifyComplete();
  }

  @Test
  void getColors_shortExpect() {
    StepVerifier.create(color.all())
        .expectNext("red", "green", "orange")
        .verifyComplete();
  }

  @Test
  void getColors_withError() {
    StepVerifier.create(color.allWithError())
        .expectNext("red")
        .expectNext("green")
        .expectNext("orange")
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void countColors_withError() {
    StepVerifier.create(color.allWithError())
        .expectNextCount(3)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  void countColors_withErrorMessage() {
    StepVerifier.create(color.allWithError())
        .expectNextCount(3)
        .expectErrorMessage("")
        .verify();
  }
}
