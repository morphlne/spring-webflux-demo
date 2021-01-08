package io.pan.webflux.controller.demo.api;

import io.pan.webflux.controller.demo.service.HotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

  @Bean
  public FluxController fluxController() {
    return new FluxController(new HotService());
  }
}
