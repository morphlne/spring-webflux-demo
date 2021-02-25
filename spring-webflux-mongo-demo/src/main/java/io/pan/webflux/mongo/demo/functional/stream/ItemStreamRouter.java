package io.pan.webflux.mongo.demo.functional.stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemStreamRouter {

  @Bean
  public RouterFunction<ServerResponse> streamRoute(ItemStreamHandler itemHandler) {
    return RouterFunctions.route(
        GET("/functional/stream/items").and(accept(MediaType.APPLICATION_JSON)),
        itemHandler::getAll
    );
  }
}
