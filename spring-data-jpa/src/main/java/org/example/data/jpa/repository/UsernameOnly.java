package org.example.data.jpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //open projections -> 따로 매칭 명시
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();

//    //close projections -> 정확하게 매칭일 경우
//    String getUsername();
}
