package io.pan.webflux.controller.demo.api;

import io.pan.webflux.controller.demo.service.HotService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxController {

  private final HotService hotService;

  public FluxController(HotService hotService) {
    this.hotService = hotService;
  }

  @GetMapping("/flux")
  public Flux<Integer> flux() {
    return reactor.core.publisher.Flux.just(1, 2, 3).log();
  }

  @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Integer> fluxStream() {
    return Flux.just(1, 2, 3)
        .delayElements(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping(value = "/infinitestream", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Long> infiniteStream() {
    return Flux.interval(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping(value = "/hotstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Long> hotStream() {
    return hotService.hotFlux();
  }
}
