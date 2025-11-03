package io.github.monty.api.auth.infrastructure.repository.jpa;

import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.model.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleJpaRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserRoleIdUserNo(String userNo);
}
