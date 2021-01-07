package io.pan.reactor.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTests {

  @Test
  public void whenCold_thenSuccess() throws InterruptedException {
    Flux<String> abc = Flux.just("A", "B", "C", "D", "E", "F")
        .delayElements(Duration.ofSeconds(1));

    abc.subscribe(s -> System.out.println("Subcriber 1 " + s));

    Thread.sleep(2000);

    abc.subscribe(s -> System.out.println("Subcriber 2 " + s));

    Thread.sleep(4000);
  }

  @Test
  public void whenHot_thenSuccess() throws InterruptedException {
    Flux<String> abc = Flux.just("A", "B", "C", "D", "E", "F")
        .delayElements(Duration.ofSeconds(1));

    ConnectableFlux<String> connectableFlux = abc.publish();
    connectableFlux.connect();

    connectableFlux.subscribe(s -> System.out.println("Subcriber 1 " + s));

    Thread.sleep(3000);

    connectableFlux.subscribe(s -> System.out.println("Subcriber 2 " + s));

    Thread.sleep(4000);
  }
}
