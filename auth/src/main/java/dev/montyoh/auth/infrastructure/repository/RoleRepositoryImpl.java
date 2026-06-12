package dev.montyoh.auth.infrastructure.repository;

import dev.montyoh.auth.domain.model.entity.Role;
import dev.montyoh.auth.domain.repository.RoleRepository;
import dev.montyoh.auth.infrastructure.repository.jpa.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<Role> findAllByIdIn(List<Long> ids) {
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
    public Role findById(Long id) {
        return roleJpaRepository.findById(id).orElse(null);
    }
}
