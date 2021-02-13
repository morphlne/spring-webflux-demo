package io.pan.webflux.mongo.demo.controller;

import io.pan.webflux.mongo.demo.document.Item;
import io.pan.webflux.mongo.demo.repository.ItemReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemReactiveRepository items;

  public ItemController(ItemReactiveRepository items) {
    this.items = items;
  }

  @GetMapping
  public Flux<Item> getAll() {
    return items.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Item>> getOne(@PathVariable String id) {
    return items.findById(id)
        .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Item> create(@RequestBody Item item) {
    return items.save(item);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Item>> update(@PathVariable String id, @RequestBody Item item) {
    return items.findById(id)
        .flatMap(
            currentItem ->
                items.save(
                    new Item(
                        currentItem.getId(),
                        item.getDescription(),
                        item.getPrice()
                    )
                )
        ).map(
            updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK)
        ).defaultIfEmpty(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
  }

  @DeleteMapping("/{id}")
  public Mono<Void> delete(@PathVariable String id) {
    return items.deleteById(id);
  }
}
