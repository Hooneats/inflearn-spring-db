package org.example.data.jpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{

    /**
     * 실무서는 BaseTimeEntity 를 상속받아  시감만 필요시에는 BaseTimeEntity를
     * 수정자도 플요하면 BaseEntity 를 상속한다.
     * */
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createDate;
//    @LastModifiedDate
//    private LocalDateTime lastModifiedDate;

    /**
     * 등록자 수정자를 쓰기 위해서는 @Bean 등록을 해줘야한다.
     * @Bean
     * public AuditorAware<String> auditorProvider() {
     *      return () -> Optional.of(UUID.randomUUID().toString());
     * }
     * */
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;
}
