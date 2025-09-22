package io.github.monty.api.auth.infrastructure.repository;

import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.repository.UserRoleRepository;
import io.github.monty.api.auth.infrastructure.repository.jpa.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleRepository {
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public List<UserRole> findByUserRoleIdUserNo(String userNo) {
        return userRoleJpaRepository.findByUserRoleIdUserNo(userNo);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleJpaRepository.save(userRole);
    }

    @Override
    public List<UserRole> saveAll(List<UserRole> userRoles) {
        return userRoleJpaRepository.saveAll(userRoles);
    }
}
