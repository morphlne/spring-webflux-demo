package io.pan.webflux.mongo.demo.initialize;

import io.pan.webflux.mongo.demo.document.Item;
import io.pan.webflux.mongo.demo.repository.ItemReactiveRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@Profile("!test")
public class ItemDataInitialization {

  private final ItemReactiveRepository items;

  public ItemDataInitialization(ItemReactiveRepository items) {
    this.items = items;
  }

  @PostConstruct
  public void initialize() {
    items.deleteAll().subscribe();
    items.saveAll(
        Arrays.asList(
            new Item(null, "first", 100.0),
            new Item(null, "second", 200.0),
            new Item(null, "third", 300.0),
            new Item("predefinedId", "description", 400.0)
        )
    ).subscribe();
  }
}
