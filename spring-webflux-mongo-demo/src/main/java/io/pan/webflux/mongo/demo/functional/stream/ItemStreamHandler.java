package io.pan.webflux.mongo.demo.functional.stream;

import io.pan.webflux.mongo.demo.document.ItemCapped;
import io.pan.webflux.mongo.demo.repository.ItemCappedReactiveRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemStreamHandler {

  private final ItemCappedReactiveRepository items;

  public ItemStreamHandler(ItemCappedReactiveRepository items) {
    this.items = items;
  }

  public Mono<ServerResponse> getAll(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(items.findAllBy(), ItemCapped.class);
  }
}
