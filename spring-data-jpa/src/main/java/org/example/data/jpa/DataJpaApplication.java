package org.example.data.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

//@EnableJpaAuditing(modifyOnCreate = false) //이거는 생성시 update 는 null 로 들어가도록 , 실무서는 사용안함
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	/**
	 * Spring Data Jpa 제공하는 수정자 등록자 기능
	 * 실제 사용시에는 Session 같은 곳에서 Id를 꺼내 사용
	 * */
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
