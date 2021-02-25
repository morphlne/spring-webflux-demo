package io.pan.webflux.mongo.demo.functional;

import io.pan.webflux.mongo.demo.document.ItemCapped;
import io.pan.webflux.mongo.demo.repository.ItemCappedReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemStreamHandlerTest {

  private final WebTestClient webTestClient;
  private final ItemCappedReactiveRepository items;
  private final ReactiveMongoOperations mongoOperations;

  @Autowired
  public ItemStreamHandlerTest(WebTestClient webTestClient, ItemCappedReactiveRepository items, ReactiveMongoOperations mongoOperations) {
    this.webTestClient = webTestClient;
    this.items = items;
    this.mongoOperations = mongoOperations;
  }

  @BeforeEach
  public void setUp() {
    mongoOperations.dropCollection(ItemCapped.class).block();
    mongoOperations.createCollection(
        ItemCapped.class,
        CollectionOptions.empty().maxDocuments(20).size(5000).capped()
    ).block();
    items.insert(
        Flux.interval(
            Duration.ofMillis(100)
        ).map(
            i -> new ItemCapped(null, "Random description " + i, 100.00 + i)
        ).take(5)
    ).blockLast();
  }

  @Test
  public void streamAll() {
    final Flux<ItemCapped> itemCapped = webTestClient.get().uri("/functional/stream/items")
        .exchange()
        .expectStatus().isOk()
        .returnResult(ItemCapped.class)
        .getResponseBody()
        .take(5);

    StepVerifier.create(itemCapped)
        .expectNextMatches(this::check)
        .thenCancel()
        .verify();
  }

  private boolean check(ItemCapped item) {
    return item.getDescription().contains("Random description");
  }
}
