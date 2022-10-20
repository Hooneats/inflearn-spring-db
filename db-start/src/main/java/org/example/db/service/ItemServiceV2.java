package org.example.db.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.db.domain.Item;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.ItemSearchCond;
import org.example.db.repository.ItemUpdateDto;
import org.example.db.repository.jpaV2.ItemQueryRepositoryV2;
import org.example.db.repository.jpaV2.ItemRepositoryV2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OCP 를 신경쓰지 않은 빠른 개발을 위한 구조
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceV2 implements ItemService {

    private final ItemRepositoryV2 itemRepositoryV2;
    private final ItemQueryRepositoryV2 itemQueryRepositoryV2;

    @Override
    public Item save(Item item) {
        return itemRepositoryV2.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = itemRepositoryV2.findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepositoryV2.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemQueryRepositoryV2.findAll(cond);
    }
}
