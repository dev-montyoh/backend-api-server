package dev.montyoh.auth.domain.repository;

import dev.montyoh.auth.domain.model.aggregate.Auth;

import java.util.Optional;

public interface AuthRepository {
    Optional<Auth> findById(String userNo);

    Auth save(Auth auth);
}
