package io.github.monty.api.auth.infrastructure.repository.redis;

import io.github.monty.api.auth.domain.model.aggregate.Auth;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<Auth, String> {
}
