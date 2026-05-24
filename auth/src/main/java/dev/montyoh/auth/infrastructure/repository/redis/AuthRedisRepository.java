package dev.montyoh.auth.infrastructure.repository.redis;

import dev.montyoh.auth.domain.model.aggregate.Auth;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<Auth, String> {
}
