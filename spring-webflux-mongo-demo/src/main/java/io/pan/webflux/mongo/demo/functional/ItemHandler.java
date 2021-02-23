package io.pan.webflux.mongo.demo.functional;

import io.pan.webflux.mongo.demo.document.Item;
import io.pan.webflux.mongo.demo.repository.ItemReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ItemHandler {

  private final ItemReactiveRepository items;

  public ItemHandler(ItemReactiveRepository items) {
    this.items = items;
  }

  public Mono<ServerResponse> getAll(ServerRequest ignored) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            items.findAll(),
            Item.class
        );
  }

  public Mono<ServerResponse> getOne(ServerRequest request) {
    return items.findById(
        request.pathVariable("id")
    ).flatMap(
        item -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(item))
    ).switchIfEmpty(
        ServerResponse.notFound().build()
    );
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    final Mono<Item> newItem = request.bodyToMono(Item.class);
    return newItem.flatMap(
        item -> ServerResponse.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                items.save(item),
                Item.class
            )
    );
  }

  public Mono<ServerResponse> update(ServerRequest request) {
    final String id = request.pathVariable("id");
    final Mono<Item> updatedItem = request.bodyToMono(Item.class)
        .flatMap(
            item -> items.findById(id)
                .flatMap(
                    currentItem -> items.save(
                        new Item(
                            currentItem.getId(),
                            item.getDescription(),
                            item.getPrice()
                        )
                    )
                )
        );
    return updatedItem.flatMap(
        item -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(item))
    ).switchIfEmpty(
        ServerResponse.notFound().build()
    );
  }

  public Mono<ServerResponse> delete(ServerRequest request) {
    final String id = request.pathVariable("id");
    final Mono<Void> deleted = items.deleteById(id);
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            deleted,
            Void.class
        );
  }

  public Mono<ServerResponse> exception(ServerRequest request) {
    throw new RuntimeException("RuntimeException occurred");
  }
}
