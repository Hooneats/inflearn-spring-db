package org.example.db.config;

import org.example.db.repository.ItemRepository;
import org.example.db.repository.memory.MemoryItemRepository;
import org.example.db.service.ItemService;
import org.example.db.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
