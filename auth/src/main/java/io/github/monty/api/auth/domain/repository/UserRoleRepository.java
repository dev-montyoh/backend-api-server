package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.domain.model.entity.UserRole;

import java.util.List;

public interface UserRoleRepository {

    List<UserRole> findByUserRoleIdUserNo(String userNo);

    UserRole save(UserRole userRole);

    List<UserRole> saveAll(List<UserRole> userRoles);
}
