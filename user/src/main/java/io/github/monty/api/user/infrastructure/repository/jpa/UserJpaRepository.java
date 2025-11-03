package io.github.monty.api.user.infrastructure.repository.jpa;

import io.github.monty.api.user.domain.model.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {
    Optional<User> findByUserNo(String userNo);

    Optional<User> findByLoginId(String loginId);
}
