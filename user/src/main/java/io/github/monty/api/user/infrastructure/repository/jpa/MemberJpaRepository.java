package io.github.monty.api.user.infrastructure.repository.jpa;

import io.github.monty.api.user.domain.model.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserNo(String userNo);

    Optional<Member> findByLoginId(String loginId);
}
