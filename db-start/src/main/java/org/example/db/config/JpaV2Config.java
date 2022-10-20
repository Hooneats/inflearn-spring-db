package org.example.db.config;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.jpa.JpaItemRepositoryV3;
import org.example.db.repository.jpaV2.ItemQueryRepositoryV2;
import org.example.db.repository.jpaV2.ItemRepositoryV2;
import org.example.db.service.ItemService;
import org.example.db.service.ItemServiceV1;
import org.example.db.service.ItemServiceV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaV2Config {

    private final EntityManager em;
    private final ItemRepositoryV2 itemRepositoryV2;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemRepositoryV2, itemQueryRepositoryV2());
    }

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepositoryV2() {
        return new ItemQueryRepositoryV2(em);
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
