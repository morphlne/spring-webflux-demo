package io.pan.webflux.client.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Item {

  private String id;
  private String description;
  private Double price;
}
