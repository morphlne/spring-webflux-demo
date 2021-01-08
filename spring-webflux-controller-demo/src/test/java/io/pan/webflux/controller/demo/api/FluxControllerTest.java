package io.pan.webflux.controller.demo.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerConfiguration.class)
@WebFluxTest
public class FluxControllerTest {

  private final WebTestClient webTestClient;

  @Autowired
  public FluxControllerTest(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  @Test
  public void whenExpectInteger_thenSuccess() {
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
  public void whenExpectListOf3_thenSuccess() {
    webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Integer.class)
        .hasSize(3);
  }

  @Test
  public void whenCompareWithExpectedList_thenSuccess() {

    List<Integer> expected = Arrays.asList(1, 2, 3);

    EntityExchangeResult<List<Integer>> result = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .returnResult();

    assertEquals(expected, result.getResponseBody());
  }

  @Test
  public void whenConsumeByAssert_thenSuccess() {

    List<Integer> expected = Arrays.asList(1, 2, 3);

    webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .consumeWith(result -> assertEquals(expected, result.getResponseBody()));
  }
}
