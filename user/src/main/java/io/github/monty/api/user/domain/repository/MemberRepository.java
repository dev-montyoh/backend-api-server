package io.github.monty.api.user.domain.repository;

import io.github.monty.api.user.domain.model.aggregate.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByUserNo(String userNo);

    Optional<Member> findByLoginId(String loginId);

    Member save(Member member);

}
