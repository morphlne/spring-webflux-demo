package io.pan.webflux.mongo.demo.repository;

import io.pan.webflux.mongo.demo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

  Flux<Item> findByDescription(String description);
}
