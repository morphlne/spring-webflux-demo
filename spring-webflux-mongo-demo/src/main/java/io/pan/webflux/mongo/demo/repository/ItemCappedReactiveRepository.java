package io.pan.webflux.mongo.demo.repository;

import io.pan.webflux.mongo.demo.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String> {

  @Tailable
  Flux<ItemCapped> findAllBy();
}
