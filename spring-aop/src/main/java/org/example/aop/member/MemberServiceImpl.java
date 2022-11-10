package org.example.aop.member;

import org.example.aop.member.annotation.ClassAop;
import org.example.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component // TODO : Spring 은 컨테이너에 빈 담을때 프록시 객체로 담기에 AOP 적용위해서는 스프링 빈으로 만들어줘야함 / 추가 설명 AopTest
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }
}
