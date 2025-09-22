package io.github.monty.api.user.domain.repository;

import io.github.monty.api.user.domain.model.aggregate.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserNo(String userNo);

    Optional<User> findByLoginId(String loginId);

    User save(User user);

}
