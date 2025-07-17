package io.github.monty.api.auth.infrastructure.repository;

import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.domain.repository.RoleRepository;
import io.github.monty.api.auth.infrastructure.repository.jpa.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<Role> findAllByIdIn(List<Integer> ids) {
        return roleJpaRepository.findAllByIdIn(ids);
    }

    @Override
    public Role save(Role role) {
        return roleJpaRepository.save(role);
    }

    @Override
    public List<Role> saveAll(List<Role> roles) {
        return roleJpaRepository.saveAll(roles);
    }

    @Override
    public Role findById(int id) {
        return roleJpaRepository.findById(id).orElse(null);
    }
}
