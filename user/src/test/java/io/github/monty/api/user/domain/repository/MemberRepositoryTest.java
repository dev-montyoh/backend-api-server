package io.github.monty.api.user.domain.repository;

import io.github.monty.api.user.domain.model.aggregate.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({UserRepositoryTestConfig.class})
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("user 조회에 성공한다.")
    void user_get_success() {
        //  given
        Member member = Member.builder()
                .userId(1)
                .userNo("testUserNo")
                .loginId("testLoginId")
                .build();
        memberRepository.save(member);

        //  when
        Optional<Member> result = memberRepository.findByUserNo(member.getUserNo());

        //  then
        assertTrue(result.isPresent());
        Member actual = result.get();
        assertThat(actual.getUserNo()).isEqualTo(member.getUserNo());
        assertThat(actual.getUserId()).isEqualTo(member.getUserId());
        assertThat(actual.getLoginId()).isEqualTo(member.getLoginId());
    }
}