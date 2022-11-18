package org.example.data.jpa.repository;

public class UsernameOnlyDto {

    private final String username;

    // DTO projections 에서는 생성자가 중요! 파라미터 명을 분석
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
