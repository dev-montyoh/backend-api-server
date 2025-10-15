package io.github.monty.api.user.infrastructure.repository;

import io.github.monty.api.user.domain.model.aggregate.Member;
import io.github.monty.api.user.domain.repository.UserRepository;
import io.github.monty.api.user.infrastructure.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Member> findByUserNo(String userNo) {
        return userJpaRepository.findByUserNo(userNo);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return userJpaRepository.findByLoginId(loginId);
    }

    @Override
    public Member save(Member member) {
        return userJpaRepository.save(member);
    }
}
