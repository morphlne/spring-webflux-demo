package io.pan.webflux.mongo.demo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {

  @Id
  private String id;
  private String description;
  private Double price;
}
