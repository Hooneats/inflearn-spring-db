package org.example.db.repository.jpa;

import org.example.db.domain.Item;
import org.example.db.repository.ItemRepository;
import org.example.db.repository.ItemSearchCond;
import org.example.db.repository.ItemUpdateDto;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Transactional 은 사실 Service 계층에서 거는게 맞다 여기서는 예제기 때문에
 *
 * Jpa 는  PersistenceException 과 그하위 예외를 발생시킨다. + 추가적으로 IllegalStateException , IllegalAtgumentException 도 발생시킬 수 있고 그 하위로 무수히 많다.
 * 스프링은 이런 에러를 @Repository 라는 어노테이션으로 추상화 인터페이스인 DataAccessException 으로 변환 시켰다.
 *
 * TODO :
 *  @Repository 의 기능
 *      - @Repository 가 붙은 클래스는 컴포넌트 스캔의 대상이 된다.
 *      - @Repository 가 붙은 클래스는 예외 변환 AOP 의 적용 대상이 된다.
 *              ㄴ 스프링은 JPA 예외 변환기('PersistenceExceptionTranslator') 를 등록한다.
 *                  ㄴ 이로인해 예외변환 AOP 프록시는 JPA 관련 예외가 발생하면 JPA 예외 변환기를 통해 스프링 데이터 접근 예외로 변환한다.
 *    ----> 스프링 부트는  'PersistenceExceptionTranslationPostProcessor' 를 자동으로 등록하는데 여기에서 @Repository 를 AOP 프록시로 만드는 어드바이저가 등록된다.
 *                              ㄴ 실제 JPA 예외를 변환하는 코드는 'EntityManagerFactoryUtils.convertJpaAccessExceptionIfPossible()' 이다.
 */
@Slf4j
@Repository
@Transactional
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
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

    // 동적쿼리 해야하지만 예제기에 그냥 findAll
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from item i";
        List<Item> result = em.createQuery(jpql, Item.class)
            .getResultList();
        return result;
    }
}
