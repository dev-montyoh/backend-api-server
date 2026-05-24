package dev.montyoh.auth.domain.repository;

import dev.montyoh.auth.domain.model.entity.UserRole;

import java.util.List;

public interface UserRoleRepository {

    List<UserRole> findByUserRoleIdUserNo(String userNo);

    UserRole save(UserRole userRole);

    List<UserRole> saveAll(List<UserRole> userRoles);
}
