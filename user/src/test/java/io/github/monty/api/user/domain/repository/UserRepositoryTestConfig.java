package io.github.monty.api.user.domain.repository;

import io.github.monty.api.user.infrastructure.repository.MemberRepositoryImpl;
import io.github.monty.api.user.infrastructure.repository.jpa.MemberJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserRepositoryTestConfig {

    @Bean
    public MemberRepository userRepository(MemberJpaRepository memberJpaRepository) {
        return new MemberRepositoryImpl(memberJpaRepository);
    }
}
