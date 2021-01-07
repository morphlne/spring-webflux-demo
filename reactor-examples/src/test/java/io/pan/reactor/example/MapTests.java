package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class MapTests {

  private final FluxColor colors;

  public MapTests() {
    this.colors = new FluxColor();
  }

  @Test
  public void whenMap_thenSuccess() {
    StepVerifier.create(
        colors.all().map(String::toUpperCase)
    ).expectNext("RED", "GREEN", "ORANGE")
        .verifyComplete();
  }

  @Test
  public void toLength_thenSuccess() {
    StepVerifier.create(
        colors.all().map(String::length)
    ).expectNext(3, 5, 6)
        .verifyComplete();
  }

  @Test
  public void whenRepeat_thenSuccess() {
    StepVerifier.create(
        colors.all()
            .map(String::length)
            .repeat(1)
    ).expectNext(
        3, 5, 6,
        3, 5, 6
    ).verifyComplete();
  }
}
