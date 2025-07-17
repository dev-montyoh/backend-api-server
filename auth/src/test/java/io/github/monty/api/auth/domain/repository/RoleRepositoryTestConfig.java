package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.infrastructure.repository.RoleRepositoryImpl;
import io.github.monty.api.auth.infrastructure.repository.jpa.RoleJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RoleRepositoryTestConfig {

    @Bean
    public RoleRepository roleRepository(RoleJpaRepository roleJpaRepository) {
        return new RoleRepositoryImpl(roleJpaRepository);
    }
}
