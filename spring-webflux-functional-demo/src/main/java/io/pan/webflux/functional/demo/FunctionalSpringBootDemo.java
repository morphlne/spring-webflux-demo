package io.pan.webflux.functional.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FunctionalSpringBootDemo {

  public static void main(String[] args) {
    new SpringApplication(FunctionalSpringBootDemo.class).run(args);
  }
}
