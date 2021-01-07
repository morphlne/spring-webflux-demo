package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class MonoColorTests {

  private final MonoColor color;

  public MonoColorTests() {
    this.color = new MonoColor();
  }

  @Test
  public void getColor_thenSuccess() {
    StepVerifier.create(color.one())
        .expectNext("red")
        .verifyComplete();
  }

  @Test
  public void expectErrorType() {
    StepVerifier.create(color.error())
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void expectErrorMessage() {
    StepVerifier.create(color.error())
        .expectErrorMessage("")
        .verify();
  }
}
