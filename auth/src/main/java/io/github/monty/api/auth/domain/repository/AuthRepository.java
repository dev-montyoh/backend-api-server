package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.domain.model.aggregate.Auth;

import java.util.Optional;

public interface AuthRepository {
    Optional<Auth> findById(String userNo);

    Auth save(Auth auth);
}
