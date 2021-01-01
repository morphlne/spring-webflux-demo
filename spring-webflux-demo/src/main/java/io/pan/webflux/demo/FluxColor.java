package io.pan.webflux.demo;

import reactor.core.publisher.Flux;

public class FluxColor {

  public Flux<String> all() {
    return Flux.just("red", "green", "orange");
  }

  public Flux<String> allWithError() {
    return all().concatWith(
        Flux.error(new RuntimeException(""))
    );
  }
}
