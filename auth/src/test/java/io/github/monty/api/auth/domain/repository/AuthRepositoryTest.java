package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.domain.model.aggregate.Auth;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataRedisTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({AuthRepositoryTestConfig.class})
class AuthRepositoryTest {

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
    private AuthRepository authRepository;

    @Test
    @DisplayName("레디스에 인증 정보를 저장한다.")
    void save() {
        //  given
        Auth auth = Auth.builder()
                .userNo("test_user_no")
                .token("test_token")
                .ttl(30000L)
                .build();

        //  when
        authRepository.save(auth);
        Optional<Auth> optionalAuth = authRepository.findById(auth.getUserNo());
        Auth actual = optionalAuth.orElse(null);

        //  then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).usingRecursiveComparison().isEqualTo(auth)
        );
    }
}