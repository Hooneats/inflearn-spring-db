package org.example.db;

import org.example.db.config.JpaConfig;
import org.example.db.config.QueryDslConfig;
import org.example.db.config.SpringDataJpaConfig;
import org.example.db.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


//@Import(JpaConfig.class)
@Import(QueryDslConfig.class)
@SpringBootApplication(scanBasePackages = "org.example.db.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

}
