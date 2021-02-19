package io.pan.webflux.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemClientApplication {

  public static void main(String[] args) {
    new SpringApplication(ItemClientApplication.class).run(args);
  }
}
