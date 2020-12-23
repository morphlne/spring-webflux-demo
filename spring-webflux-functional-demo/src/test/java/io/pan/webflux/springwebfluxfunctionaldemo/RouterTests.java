package io.pan.webflux.springwebfluxfunctionaldemo;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class RouterTests {

  private final WebTestClient webClient;

  public RouterTests() {
    this.webClient = WebTestClient.bindToRouterFunction(
        new Router().get()
    ).build();
  }

  @Test
  public void indexPage_whenRequest_thenSuccess() {
    webClient.get().uri("/").exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .isEqualTo("Hello");
  }

  @Test
  public void jsonPage_whenRequest_thenSuccess() {
    webClient.get().uri("/json").exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(Greeting.class)
        .isEqualTo(new Greeting("Hello"));
  }
}
