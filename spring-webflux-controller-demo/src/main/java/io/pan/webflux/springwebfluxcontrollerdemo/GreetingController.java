package io.pan.webflux.springwebfluxcontrollerdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

  private final GreetingRepository greetings;

  public GreetingController(GreetingRepository greetings) {
    this.greetings = greetings;
  }

  @GetMapping("/{id}")
  public Mono<Greeting> getById(@PathVariable String id) {
    return greetings.getById(id);
  }

  @GetMapping
  public Flux<Greeting> getAll() {
    return greetings.getAll();
  }
}
