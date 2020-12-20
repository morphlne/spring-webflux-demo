package io.pan.webflux.springwebfluxfunctionaldemo;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class SpringWebfluxFunctionalDemo {

  public static void main(String[] args) throws InterruptedException {

    HandlerFunction<ServerResponse> okResponse = request -> ServerResponse.ok().body(fromValue("Hello"));

    RouterFunction<ServerResponse> router = route(GET("/"), okResponse);

    HttpHandler httpHandler = RouterFunctions.toHttpHandler(router);

    HttpServer
        .create().host("localhost").port(8080)
        .handle(new ReactorHttpHandlerAdapter(httpHandler))
        .bindNow();

    Thread.currentThread().join();
  }
}
