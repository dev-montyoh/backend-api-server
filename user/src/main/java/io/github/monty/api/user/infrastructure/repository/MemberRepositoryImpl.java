package io.github.monty.api.user.infrastructure.repository;

import io.github.monty.api.user.domain.model.aggregate.Member;
import io.github.monty.api.user.domain.repository.MemberRepository;
import io.github.monty.api.user.infrastructure.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findByUserNo(String userNo) {
        return memberJpaRepository.findByUserNo(userNo);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberJpaRepository.findByLoginId(loginId);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }
}
