package org.example.db.repository.jpa;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.db.domain.Item;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.ItemSearchCond;
import org.example.db.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Override
    public Item save(Item item) {
        return springDataJpaItemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = springDataJpaItemRepository.findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return springDataJpaItemRepository.findById(id);
    }

    // 동적쿼리 해야하지만 예제기에 그냥 findAll
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return springDataJpaItemRepository.findAll();
    }
}
