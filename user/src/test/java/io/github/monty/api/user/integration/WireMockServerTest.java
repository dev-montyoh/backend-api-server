package io.github.monty.api.user.integration;

import io.github.monty.api.user.common.utils.EncryptUtil;
import io.github.monty.api.user.domain.model.aggregate.Member;
import io.github.monty.api.user.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

/**
 * IntegrationTest 를 위한 공통 WireMockServer 세팅 클래스
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(stubs = "classpath:/stubs/**/*.json")
@ActiveProfiles("test")
public class WireMockServerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    protected TestRestTemplate restTemplate;

    /**
     * DB에 UserData 세팅
     *
     * @param userNo   회원 번호
     * @param loginId  로그인 ID
     * @param password 비밀번호
     */
    protected void insertUserData(String userNo, String loginId, String password) {
        String encryptedPassword = EncryptUtil.encode(password);
        Member member = Member.builder()
                .userNo(userNo)
                .loginId(loginId)
                .password(encryptedPassword)
                .build();
        memberRepository.save(member);
    }
}
