package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.infrastructure.repository.UserRoleRepositoryImpl;
import io.github.monty.api.auth.infrastructure.repository.jpa.UserRoleJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserRoleRepositoryTestConfig {

    @Bean
    public UserRoleRepository userRoleRepository(UserRoleJpaRepository userRoleJpaRepository) {
        return new UserRoleRepositoryImpl(userRoleJpaRepository);
    }
}
