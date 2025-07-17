package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.infrastructure.repository.AuthRepositoryImpl;
import io.github.monty.api.auth.infrastructure.repository.redis.AuthRedisRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthRepositoryTestConfig {

    @Bean
    public AuthRepository authRepository(AuthRedisRepository authRedisRepository) {
        return new AuthRepositoryImpl(authRedisRepository);
    }
}
