package dev.montyoh.auth.infrastructure.repository.jpa;

import dev.montyoh.auth.domain.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByIdIn(List<Integer> ids);
}
