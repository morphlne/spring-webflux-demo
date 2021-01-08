package io.pan.webflux.controller.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class HotService {

  private final ConnectableFlux<Long> flux;

  public HotService(ConnectableFlux<Long> flux) {
    this.flux = flux;
  }

  @Autowired
  public HotService() {
    this(
        Flux.interval(Duration.ofSeconds(1))
            .log()
            .publish()
    );
  }

  @PostConstruct
  private void streamInitialize() {
    flux.connect();
  }

  public Flux<Long> hotFlux() {
    return flux;
  }
}
