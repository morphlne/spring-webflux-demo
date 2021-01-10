package io.pan.webflux.functional.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class HandlerFunctionTest {

  private final WebTestClient webTestClient;

  @Autowired
  public HandlerFunctionTest(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  @Test
  public void whenExpectFluxInteger_thenSuccess() {
    Flux<Integer> flux = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Integer.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectSubscription()
        .expectNext(1, 2, 3)
        .verifyComplete();
  }

  @Test
  public void whenExpectMonoInteger_thenSuccess() {
    final Integer expected = 1;

    webTestClient.get().uri("/mono")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Integer.class)
        .consumeWith(
            response -> assertEquals(expected, response.getResponseBody())
        );
  }
}
