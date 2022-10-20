package org.example.db.repository.jpa;

import java.util.List;
import org.example.db.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    // 사용은 SpringDataJpaItemRepository.findItems("%"+ItemName+"%", price);
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String ItemName, Integer price);

    // 사용은 SpringDataJpaItemRepository.findItems("%"+ItemName+"%", price);
    @Query("select i from item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String ItemName, @Param("price") Integer price);

}
