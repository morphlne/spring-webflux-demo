package io.pan.webflux.mongo.demo.controller;

import io.pan.webflux.mongo.demo.document.ItemCapped;
import io.pan.webflux.mongo.demo.repository.ItemCappedReactiveRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stream/items")
public class ItemStreamController {

  private final ItemCappedReactiveRepository items;

  public ItemStreamController(ItemCappedReactiveRepository items) {
    this.items = items;
  }

  @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<ItemCapped> getAll() {
    return items.findAllBy();
  }
}
