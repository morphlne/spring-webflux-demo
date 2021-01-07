package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FilterTests {

  private final FluxColor colors;

  public FilterTests() {
    this.colors = new FluxColor();
  }

  @Test
  public void whenFilter_thenSuccess() {
    StepVerifier.create(
        colors.all().filter(
            s -> s.startsWith("g")
        )
    ).expectNext("green")
        .verifyComplete();
  }

  @Test
  public void whenFilterLength_thenSuccess() {
    StepVerifier.create(
        colors.all().filter(
            s -> s.length() > 3
        )
    ).expectNext("green", "orange")
        .verifyComplete();
  }
}
