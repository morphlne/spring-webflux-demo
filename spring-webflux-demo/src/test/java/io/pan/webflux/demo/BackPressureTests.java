package io.pan.webflux.demo;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTests {

  @Test
  public void whenRequest_thenSuccess() {
    Flux<String> abc = Flux.just("A", "B", "C");

    StepVerifier.create(abc)
        .expectSubscription()
        .thenRequest(1)
        .expectNext("A")
        .thenRequest(1)
        .expectNext("B")
        .thenCancel()
        .verify();
  }

  @Test
  public void whenSubscribe_thenSuccess() {
    Flux.just("A", "B", "C")
        .subscribeWith(
            new CustomSubscriber() {
              @Override
              public void onSubscribe(Subscription s) {
                s.request(2); //3 -> onComplete -> Done
              }
            }
        );
  }

  @Test
  public void whenCancel_thenSuccess() {
    Flux.just("A", "B", "C")
        .log()
        .subscribeWith(
            new CustomSubscriber() {
              @Override
              public void onSubscribe(Subscription s) {
                s.cancel();
              }
            }
        );
  }

  @Test
  public void whenHookOnNext_thenSuccess() {
    Flux.just("A", "B", "C")
        .log()
        .subscribeWith(
            new BaseSubscriber<String>() {
              @Override
              protected void hookOnNext(String value) {
                if (value.equals("B")) {
                  cancel();
                }
                request(1);
              }
            }
        );
  }

  private abstract class CustomSubscriber implements Subscriber<String> {
    public abstract void onSubscribe(Subscription s);

    @Override
    public void onNext(String s) {
      System.out.println(s);
    }

    @Override
    public void onError(Throwable t) {
      t.printStackTrace();
    }

    @Override
    public void onComplete() {
      System.out.println("Done");
    }
  }
}
