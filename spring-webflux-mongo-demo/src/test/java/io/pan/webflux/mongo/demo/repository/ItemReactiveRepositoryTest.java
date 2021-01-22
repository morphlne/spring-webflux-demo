package io.pan.webflux.mongo.demo.repository;

import io.pan.webflux.mongo.demo.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ItemReactiveRepositoryTest {

  private final ItemReactiveRepository items;

  @Autowired
  public ItemReactiveRepositoryTest(ItemReactiveRepository items) {
    this.items = items;
  }

  @BeforeEach
  public void setUp() {
    items.deleteAll().thenMany(
        Flux.fromArray(
            new Item[]{
                new Item(null, "first", 100.0),
                new Item(null, "second", 200.0),
                new Item(null, "third", 300.0),
                new Item("predefinedId", "description", 400.0)
            })
    ).flatMap(items::save)
        .blockLast();
  }

  @Test
  public void getAll() {
    StepVerifier.create(
        items.findAll()
    ).expectSubscription()
        .expectNextCount(4)
        .verifyComplete();
  }

  @Test
  public void getById() {
    StepVerifier.create(
        items.findById("predefinedId")
    ).expectSubscription()
        .expectNextMatches(item -> item.getDescription().equals("description"))
        .verifyComplete();
  }

  @Test
  public void getByDescription() {
    StepVerifier.create(
        items.findByDescription("description")
    ).expectSubscription()
        .expectNextMatches(item -> item.getId().equals("predefinedId"))
        .verifyComplete();
  }

  @Test
  public void save() {
    Mono<Item> persistedItem = items.save(new Item(null, "persisted", 500.0));
    StepVerifier.create(persistedItem)
        .expectSubscription()
        .expectNextMatches(
            item -> item.getId() != null
                && item.getDescription().equals("persisted")
        );
  }

  @Test
  public void update() {
    final double newPrice = 199.0;
    Flux<Item> updatedItem = items.findByDescription("second")
        .map(item -> new Item(item.getId(), item.getDescription(), newPrice))
        .flatMap(items::save);

    StepVerifier.create(updatedItem)
        .expectSubscription()
        .expectNextMatches(item -> item.getPrice() == newPrice);
  }

  @Test
  public void delete() {
    Mono<Void> deleted = items.deleteById("predefinedId");

    StepVerifier.create(deleted)
        .expectSubscription()
        .verifyComplete();

    StepVerifier.create(items.findAll())
        .expectSubscription()
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  public void deleteByCondition() {
    Flux<Void> deleted = items.findAll()
        .filter(item -> (item.getPrice() > 200.0))
        .flatMap(items::delete);

    StepVerifier.create(deleted)
        .expectSubscription()
        .verifyComplete();

    StepVerifier.create(items.findAll())
        .expectSubscription()
        .expectNextCount(2)
        .verifyComplete();
  }
}
