package dev.montyoh.auth.infrastructure.repository.jpa;

import dev.montyoh.auth.domain.model.entity.UserRole;
import dev.montyoh.auth.domain.model.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleJpaRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUserRoleIdUserNo(String userNo);
}
