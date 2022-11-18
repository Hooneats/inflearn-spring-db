package org.example.data.jpa.repository;

import org.example.data.jpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() throws Exception{
        // given
        Item item = new Item("A");
        // when
        itemRepository.save(item);
        // then

    }

}