package io.pan.webflux.springwebfluxcontrollerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SpringWebfluxControllerDemo {

  public static void main(String[] args) {
    new SpringApplication(SpringWebfluxControllerDemo.class).run(args);
    WebClient client = WebClient.create("http://localhost:8080");
  }
}
