package io.github.monty.api.auth.integration;

import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.model.entity.UserRoleId;
import io.github.monty.api.auth.domain.repository.RoleRepository;
import io.github.monty.api.auth.domain.repository.UserRoleRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * IntegrationTest 를 위한 공통 WireMockServer 세팅 클래스
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(stubs = "classpath:/stubs/**/*.json")
@ActiveProfiles("test")
public class WireMockServerTest {

    private static RedisServer redisServer;

    @BeforeAll
    public static void setUp() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Role 에 관한 데이터 insert
     *
     * @param name        역할 이름
     * @param description 역할 설명
     * @return 저장 결과
     */
    protected Role insertRoleData(String name, String description) {
        Role role = Role.builder()
                .name(name)
                .description(description)
                .build();
        return roleRepository.save(role);
    }

    /**
     * @param userNo 회원 번호
     * @param role   저장하고자 하는 권한(Role)
     */
    protected void insertUserRoleData(String userNo, Role role) {
        UserRoleId userRoleId = UserRoleId.builder()
                .userNo(userNo)
                .roleId(role.getId())
                .build();
        UserRole userRole = UserRole.builder()
                .userRoleId(userRoleId)
                .role(role)
                .build();
        userRoleRepository.save(userRole);
    }

}
