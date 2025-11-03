package io.github.monty.api.auth.infrastructure.repository.jpa;

import io.github.monty.api.auth.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByIdIn(List<Integer> ids);
}
