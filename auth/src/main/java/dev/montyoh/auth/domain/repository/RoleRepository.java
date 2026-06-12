package dev.montyoh.auth.domain.repository;

import dev.montyoh.auth.domain.model.entity.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> findAllByIdIn(List<Long> ids);

    Role save(Role role);

    List<Role> saveAll(List<Role> roles);

    Role findById(Long id);
}
