package io.pan.webflux.springwebfluxfunctionaldemo;

public class Greeting {

  private final String hallo;

  public Greeting(String hallo) {
    this.hallo = hallo;
  }

  public String getHallo() {
    return hallo;
  }
}
