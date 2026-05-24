package dev.montyoh.auth.domain.repository;

import dev.montyoh.auth.infrastructure.repository.AuthRepositoryImpl;
import dev.montyoh.auth.infrastructure.repository.redis.AuthRedisRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AuthRepositoryTestConfig {

    @Bean
    public AuthRepository authRepository(AuthRedisRepository authRedisRepository) {
        return new AuthRepositoryImpl(authRedisRepository);
    }
}
