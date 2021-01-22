package io.pan.webflux.mongo.demo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@AllArgsConstructor
@ToString
public class Item {

  @Id
  private final String id;
  private final String description;
  private final Double price;
}
