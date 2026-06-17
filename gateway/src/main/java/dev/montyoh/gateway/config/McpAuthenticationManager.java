package dev.montyoh.gateway.config;

import dev.montyoh.gateway.common.constant.ErrorCode;
import dev.montyoh.gateway.common.constant.StaticValues;
import dev.montyoh.gateway.common.exception.ApplicationException;
import dev.montyoh.gateway.common.exception.InvalidTokenException;
import dev.montyoh.gateway.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class McpAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String accessToken = authentication.getCredentials().toString();
        if (StringUtils.hasText(accessToken)) {
            Jws<Claims> claims = this.parsingToken(accessToken);
            List<SimpleGrantedAuthority> authorities = this.getUserRoleFromClaims(claims);
            UserDetails user = new User("user", "", authorities);
            return Mono.just(new UsernamePasswordAuthenticationToken(user, accessToken, user.getAuthorities()));
        }
        return Mono.empty();
    }

    private Jws<Claims> parsingToken(String token) {
        try {
            return jwtUtils.parsingMcpToken(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(ErrorCode.TOKEN_EXPIRED_ERROR);
        } catch (SignatureException | MalformedJwtException e) {
            throw new InvalidTokenException(ErrorCode.TOKEN_STATUS_ERROR);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private List<SimpleGrantedAuthority> getUserRoleFromClaims(Jws<Claims> tokenClaims) {
        List<?> userRoleList = (List<?>) tokenClaims.getPayload().get(StaticValues.TOKEN_PAYLOAD_USER_ROLE_KEY);
        return userRoleList.stream()
                .map(value -> new SimpleGrantedAuthority((String) value))
                .collect(Collectors.toList());
    }
}
