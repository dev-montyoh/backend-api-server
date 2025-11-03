package io.github.monty.api.user.domain.repository;

import io.github.monty.api.user.infrastructure.repository.UserRepositoryImpl;
import io.github.monty.api.user.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserRepositoryTestConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }
}
