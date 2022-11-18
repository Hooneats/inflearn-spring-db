package org.example.data.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.example.data.jpa.dto.MemberDto;
import org.example.data.jpa.entity.Member;
import org.example.data.jpa.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
    /**
     * Web 확장 - 도메인 클래스 컨버터
     * 위의 메소드 과정을 SpringData Jpa 가 해준다.
     * 하지만 이기능은 권장하지 않는다.
     * */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }


    /**
     * Web 확장 - 페이징과 정렬
     * localhost:8080/members?page=0
     * 으로 기본 20 개씩 가져다 준다.
     * localhost:8080/members?page=0&size=3
     * 이렇게 size 지정도가능
     * localhost:8080/members?page=0&size=3&sort=id,desc
     * 이렇게 정렬도가능
     * localhost:8080/members?page=0&size=3&sort=id,desc&sort=username
     * 도 가능
     * 컨트롤러에서 바인딩 될때 Pageable 이 있으면 PageRequest 를 만들어 반환해줌
     * 추가적으로 하지만 Entity 를 밖에 노출하면 안되기에 다시 DTO 로 변환하여 반환하자!
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5,sort = "username") Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
//        return map;
        // 위와 동일
//        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member));
        // 위와 동일
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
