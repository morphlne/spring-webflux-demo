package io.pan.webflux.mongo.demo.controller;

import io.pan.webflux.mongo.demo.document.Item;
import io.pan.webflux.mongo.demo.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemControllerTest {

  private final WebTestClient webTestClient;
  private final ItemReactiveRepository items;

  @Autowired
  public ItemControllerTest(WebTestClient webTestClient, ItemReactiveRepository items) {
    this.webTestClient = webTestClient;
    this.items = items;
  }

  @BeforeEach
  public void setUp() {
    items.deleteAll().block();
    items.saveAll(
        Arrays.asList(
            new Item(null, "first", 100.0),
            new Item(null, "second", 200.0),
            new Item(null, "third", 300.0),
            new Item("predefinedId", "description", 400.0)
        )
    ).blockLast();
  }

  @Test
  public void getAll() {
    webTestClient.get().uri("/items")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Item.class)
        .hasSize(4);
  }

  @Test
  public void getAll_checkIds() {
    webTestClient.get().uri("/items")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Item.class)
        .hasSize(4)
        .consumeWith(
            response -> response.getResponseBody().forEach(
                item -> assertNotNull(item.getId())
            )
        );
  }

  @Test
  public void getAll_Flux() {
    Flux<Item> itemFlux = webTestClient.get().uri("/items")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .returnResult(Item.class)
        .getResponseBody();

    StepVerifier.create(itemFlux)
        .expectSubscription()
        .expectNextCount(4)
        .verifyComplete();
  }

  @Test
  public void getOne() {
    webTestClient.get().uri("/items/{id}", "predefinedId")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.price", 400.0);
  }

  @Test
  public void getOne_notFound() {
    webTestClient.get().uri("/items/{id}", "wrongId")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void create() {
    final String newItemDescription = "newItem";
    final double newItemPrice = 500.0;
    webTestClient.post().uri("/items")
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            Mono.just(
                new Item(
                    null,
                    newItemDescription,
                    newItemPrice
                )
            ), Item.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.description").isEqualTo(newItemDescription)
        .jsonPath("$.price").isEqualTo(newItemPrice);
  }

  @Test
  public void update() {
    final double discountPrice = 399.99;
    webTestClient.put().uri("/items/{id}", "predefinedId")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(
            Mono.just(
                new Item(
                    null,
                    "discountDescription",
                    discountPrice
                )
            ), Item.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.price", discountPrice);
  }

  @Test
  public void update_wrongId() {
    final double discountPrice = 399.99;
    webTestClient.put().uri("/items/{id}", "wrongId")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(
            Mono.just(
                new Item(
                    null,
                    "discountDescription",
                    discountPrice
                )
            ), Item.class)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void delete() {
    webTestClient.delete().uri("/items/{id}", "predefinedId")
        .exchange()
        .expectStatus().isOk()
        .expectBody(Void.class);
  }
}
