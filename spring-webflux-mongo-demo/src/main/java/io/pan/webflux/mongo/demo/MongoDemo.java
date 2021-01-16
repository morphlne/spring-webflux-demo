package io.pan.webflux.mongo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoDemo {

  public static void main(String[] args) {
    new SpringApplication(MongoDemo.class).run(args);
  }
}
