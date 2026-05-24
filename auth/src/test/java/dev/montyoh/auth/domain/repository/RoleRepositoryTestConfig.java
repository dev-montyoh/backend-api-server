package dev.montyoh.auth.domain.repository;

import dev.montyoh.auth.infrastructure.repository.RoleRepositoryImpl;
import dev.montyoh.auth.infrastructure.repository.jpa.RoleJpaRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RoleRepositoryTestConfig {

    @Bean
    public RoleRepository roleRepository(RoleJpaRepository roleJpaRepository) {
        return new RoleRepositoryImpl(roleJpaRepository);
    }
}
