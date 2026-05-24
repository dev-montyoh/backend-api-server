package dev.montyoh.auth.infrastructure.repository;

import dev.montyoh.auth.domain.model.aggregate.Auth;
import dev.montyoh.auth.domain.repository.AuthRepository;
import dev.montyoh.auth.infrastructure.repository.redis.AuthRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
    private final AuthRedisRepository authRedisRepository;

    @Override
    public Optional<Auth> findById(String userNo) {
        return authRedisRepository.findById(userNo);
    }

    @Override
    public Auth save(Auth auth) {
        return authRedisRepository.save(auth);
    }
}
