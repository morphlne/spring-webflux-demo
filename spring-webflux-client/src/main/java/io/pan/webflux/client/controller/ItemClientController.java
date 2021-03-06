package io.pan.webflux.client.controller;

import io.pan.webflux.client.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/items")
public class ItemClientController {

  private final WebClient items;
  private final Logger logger;

  public ItemClientController(WebClient items) {
    this.items = items;
    this.logger = LoggerFactory.getLogger(ItemClientController.class);
  }

  @Autowired
  public ItemClientController(@Value("${mongo-demo-url}") String url) {
    this(WebClient.create(url));
  }

  @GetMapping("/retrieve")
  public Flux<Item> getAll_retrieve() {
    return items.get().uri("/items")
        .retrieve()
        .bodyToFlux(Item.class)
        .log("Retrieve: ");
  }

  @GetMapping("/exchange")
  public Flux<Item> getAll_exchange() {
    return items.get().uri("/items")
        .exchangeToFlux(
            response -> response.bodyToFlux(Item.class)
        ).log("Exchange: ");
  }

  @GetMapping("/{id}")
  public Mono<Item> getOne(@PathVariable String id) {
    return items.get().uri("/items/{id}", id)
        .retrieve()
        .bodyToMono(Item.class)
        .log("Retrieve one: ");
  }

  @PostMapping
  public Mono<Item> create(@RequestBody Item item) {
    return items.post().uri("/items")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(item), Item.class)
        .retrieve()
        .bodyToMono(Item.class)
        .log("Created: ");
  }

  @PutMapping("/{id}")
  public Mono<Item> update(@PathVariable String id, @RequestBody Item item) {
    if (!Objects.equals(id, item.getId())) {
      throw new IllegalArgumentException();
    }
    return items.put().uri("/items/{id}", id)
        .body(Mono.just(item), Item.class)
        .retrieve()
        .bodyToMono(Item.class)
        .log("Updated: ");
  }

  @DeleteMapping("/{id}")
  public Mono<Void> delete(@PathVariable String id) {
    return items.delete().uri("/items/{id}", id)
        .retrieve()
        .bodyToMono(Void.class)
        .log("Deleted: ");
  }

  @GetMapping("/retrieve/error")
  public Flux<Item> retrieveError() {
    return items.get().uri("functional/exception")
        .retrieve()
        .onStatus(
            HttpStatus::is5xxServerError,
            clientResponse -> {
              Mono<String> error = clientResponse.bodyToMono(String.class);
              return error.flatMap(
                  message -> {
                    logger.error(String.format("Error: %s", message));
                    throw new RuntimeException(message);
                  }
              );
            }
        ).bodyToFlux(Item.class);
  }

  @GetMapping("/exchange/error")
  public Flux<Item> exchangeError() {
    return items.get().uri("functional/exception")
        .exchangeToFlux(
            clientResponse -> clientResponse.statusCode().is5xxServerError()
                ? clientResponse.bodyToFlux(String.class)
                .flatMap(
                    message -> {
                      logger.error(String.format("Error: %s", message));
                      throw new RuntimeException(message);
                    }
                )
                : clientResponse.bodyToFlux(Item.class)
        );
  }
}
