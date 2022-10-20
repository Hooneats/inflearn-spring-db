package org.example.db.repository.jpa;

import static org.example.db.domain.QItem.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.example.db.domain.Item;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.ItemSearchCond;
import org.example.db.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = em.find(Item.class, itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

//    @Override
    public List<Item> findAllOld(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        QItem item = QItem.item;
        // 동적 쿼리
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(cond.getItemName())) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice));
        }

        return query.select(item)
            .from(item)
            .where(builder)
            .fetch();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        QItem item = QItem.item;
        // 동적 쿼리 BooleanBuilder 사용
//        BooleanBuilder builder = new BooleanBuilder();
//        if (StringUtils.hasText(itemName)) {
//            builder.and(item.itemName.like("%" + itemName + "%"));
//        }
//        if (maxPrice != null) {
//            builder.and(item.price.loe(maxPrice));
//        }

        return query.select(item)
            .from(item)
//            .where(builder)
            .where(likeItemName(itemName), maxPrice(maxPrice))
            .fetch();

    }

    private BooleanExpression likeItemName(String itemName) {
        return StringUtils.hasText(itemName) ? item.itemName.like("%" + itemName + "%") : null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        return maxPrice != null ? item.price.loe(maxPrice) : null;
    }
}
