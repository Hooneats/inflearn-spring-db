package org.example.db.repository.jpaV2;

import org.example.db.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {

}
