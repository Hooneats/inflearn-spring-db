package org.example.db.repository.jpaV2;

import static org.example.db.domain.QItem.*;
import static org.example.db.domain.QItem.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.example.db.domain.Item;
import org.example.db.domain.QItem;
import org.example.db.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ItemQueryRepositoryV2 {

    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        return query.select(item)
            .from(item)
            .where(likeItemName(cond.getItemName()), maxPrice(cond.getMaxPrice()))
            .fetch();

    }

    private BooleanExpression likeItemName(String itemName) {
        return StringUtils.hasText(itemName) ? item.itemName.like("%" + itemName + "%") : null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        return maxPrice != null ? item.price.loe(maxPrice) : null;
    }

}
