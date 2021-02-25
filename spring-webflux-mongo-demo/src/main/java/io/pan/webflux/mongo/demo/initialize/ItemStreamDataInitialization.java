package io.pan.webflux.mongo.demo.initialize;

import io.pan.webflux.mongo.demo.document.ItemCapped;
import io.pan.webflux.mongo.demo.repository.ItemCappedReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
@Profile("!test")
@Slf4j
public class ItemStreamDataInitialization {

  private final ItemCappedReactiveRepository cappedItems;
  private final ReactiveMongoOperations mongoOperations;

  public ItemStreamDataInitialization(ItemCappedReactiveRepository cappedItems, ReactiveMongoOperations mongoOperations) {
    this.cappedItems = cappedItems;
    this.mongoOperations = mongoOperations;
  }

  @PostConstruct
  public void initialize() {
    creteCappedCollection();
    initializeCappedCollection();
  }

  private void creteCappedCollection() {
    mongoOperations.dropCollection(ItemCapped.class).then(
        mongoOperations.createCollection(
            ItemCapped.class,
            CollectionOptions.empty().maxDocuments(20).size(50000).capped()
        )
    );
  }

  private void initializeCappedCollection() {
    Flux<ItemCapped> itemCappedFlux = Flux.interval(
        Duration.ofSeconds(1)
    ).map(
        i -> new ItemCapped(null, "Random description " + i, 100.00 + i)
    );
    cappedItems.insert(itemCappedFlux)
        .subscribe(
            itemCapped -> log.info("Inserted item: " + itemCapped)
        );
  }
}
