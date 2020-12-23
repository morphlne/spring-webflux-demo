package io.pan.webflux.springwebfluxfunctionaldemo;

import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.netty.http.server.HttpServer;

public class SpringWebfluxFunctionalDemo {

  public static void main(String[] args) throws InterruptedException {

    HttpServer
        .create().host("localhost").port(8080)
        .handle(new ReactorHttpHandlerAdapter(
            RouterFunctions.toHttpHandler(new Router().get()))
        ).bindNow();

    Thread.currentThread().join();
  }
}
