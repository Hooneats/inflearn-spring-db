package org.example.db.config;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.jpa.JpaItemRepository;
import org.example.db.repository.memory.MemoryItemRepository;
import org.example.db.service.ItemService;
import org.example.db.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager em;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepository(em);
    }
}
