package io.github.monty.api.auth.domain.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Duration;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken")
public class Auth {
    @Id
    private String userNo;

    @Indexed
    private String token;

    @TimeToLive
    private long ttl;

    public Auth(String userNo, Duration ttlMinute) {
        this.userNo = userNo;
        this.ttl = ttlMinute.getSeconds();
    }

    public void changeRefreshToken(String token) {
        this.token = token;
    }
}
