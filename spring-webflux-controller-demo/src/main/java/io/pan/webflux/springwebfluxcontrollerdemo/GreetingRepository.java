package io.pan.webflux.springwebfluxcontrollerdemo;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GreetingRepository {

  private final Map<String, Greeting> greetings;

  public GreetingRepository(Map<String, Greeting> greetings) {
    this.greetings = greetings;
  }

  public GreetingRepository() {
    this(init());
  }

  static private Map<String, Greeting> init() {
    Map<String, Greeting> init = new HashMap<>();
    init.put("1", new Greeting("First hi"));
    init.put("2", new Greeting("Second hello"));
    return init;
  }

  public Mono<Greeting> getById(String id) {
    return Mono.just(greetings.get(id));
  }

  public Flux<Greeting> getAll() {
    return Flux.fromIterable(greetings.values());
  }
}
