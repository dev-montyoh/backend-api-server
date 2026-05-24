package dev.montyoh.user.domain.repository;

import dev.montyoh.user.domain.model.aggregate.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserNo(String userNo);

    Optional<User> findByLoginId(String loginId);

    User save(User user);

}
