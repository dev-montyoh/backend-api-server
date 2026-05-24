package dev.montyoh.user.domain.repository;

import dev.montyoh.user.infrastructure.repository.UserRepositoryImpl;
import dev.montyoh.user.infrastructure.repository.jpa.UserJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserRepositoryTestConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }
}
