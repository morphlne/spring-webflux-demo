package io.pan.webflux.springwebfluxfunctionaldemo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Greeting {

  private final String hallo;

  public Greeting(@JsonProperty("hallo") String hallo) {
    this.hallo = hallo;
  }

  public String getHallo() {
    return hallo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Greeting greeting = (Greeting) o;

    return Objects.equals(hallo, greeting.hallo);
  }
}
