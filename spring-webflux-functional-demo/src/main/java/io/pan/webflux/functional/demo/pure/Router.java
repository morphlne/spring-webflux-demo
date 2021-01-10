package io.pan.webflux.functional.demo.pure;

import io.pan.webflux.functional.demo.pure.Greeting;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Supplier;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class Router implements Supplier<RouterFunction<ServerResponse>> {

  private final HandlerFunction<ServerResponse> okString;
  private final HandlerFunction<ServerResponse> okJson;

  public Router(HandlerFunction<ServerResponse> okString, HandlerFunction<ServerResponse> okJson) {
    this.okString = okString;
    this.okJson = okJson;
  }

  public Router() {
    this(
        request -> ServerResponse.ok().body(fromValue("Hello")),
        request -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(new Greeting("Hello")))
    );
  }

  @Override
  public RouterFunction<ServerResponse> get() {
    return route(
        GET("/"), okString
    ).andRoute(
        GET("/json"), okJson
    );
  }
}
