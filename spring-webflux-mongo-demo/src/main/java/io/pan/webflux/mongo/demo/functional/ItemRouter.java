package io.pan.webflux.mongo.demo.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemRouter {

  @Bean
  public RouterFunction<ServerResponse> itemRoute(ItemHandler itemHandler) {
    return RouterFunctions.route(
        GET("functional/items").and(
            accept(MediaType.APPLICATION_JSON)
        ),
        itemHandler::getAll
    ).andRoute(
        GET("functional/items/{id}").and(
            accept(MediaType.APPLICATION_JSON)
        ),
        itemHandler::getOne
    ).andRoute(
        POST("functional/items").and(
            accept(MediaType.APPLICATION_JSON)
        ),
        itemHandler::create
    ).andRoute(
        DELETE("functional/items/{id}").and(
            accept(MediaType.APPLICATION_JSON)
        ),
        itemHandler::delete
    ).andRoute(
        PUT("functional/items/{id}").and(
            accept(MediaType.APPLICATION_JSON)
        ),
        itemHandler::update
    );
  }
}
