package org.example.db.service;

import org.example.db.repository.ItemSearchCond;
import org.example.db.repository.ItemUpdateDto;
import org.example.db.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
