package io.pan.reactor.example;

import reactor.core.publisher.Mono;

public class MonoColor {

  public Mono<String> one() {
    return Mono.just("red");
  }

  public Mono<String> error() {
    return Mono.error(new RuntimeException(""));
  }
}
