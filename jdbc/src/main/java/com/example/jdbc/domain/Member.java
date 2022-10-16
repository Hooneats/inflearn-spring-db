package com.example.jdbc.domain;

import com.example.jdbc.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Member {

    private String memberId;
    private int money;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .memberId(this.memberId)
                .money(this.money)
                .build();
    }

    public static Member of(MemberEntity memberEntity) {
        return Member.builder()
                .memberId(memberEntity.getMemberId())
                .money(memberEntity.getMoney())
                .build();
    }


}
