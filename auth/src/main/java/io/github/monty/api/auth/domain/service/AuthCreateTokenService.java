package io.github.monty.api.auth.domain.service;

import io.github.monty.api.auth.common.constants.ErrorCode;
import io.github.monty.api.auth.common.constants.StaticValues;
import io.github.monty.api.auth.common.exception.ApplicationException;
import io.github.monty.api.auth.common.utils.JwtUtils;
import io.github.monty.api.auth.domain.model.aggregate.Auth;
import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.domain.repository.AuthRepository;
import io.github.monty.api.auth.domain.repository.RoleRepository;
import io.github.monty.api.auth.domain.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthCreateTokenService {

    @Value("${redis.refresh-token.ttl}")
    private int refreshKeyTtl;

    private final UserRoleRepository userRoleRepository;
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    /**
     * 인증에 필요한 엑세스 토큰과 리프레시 토큰을 발급하고, 반환한다.
     * 리프레시 토큰은 userNo 에 맞게 저장한다.
     *
     * @param userNo 회원 번호
     * @return 액세스 토큰과 리프레시 토큰
     */
    public AuthCreateTokenVo createTokenAndSaveRefreshToken(String userNo) {
        List<String> userRoleList = this.getUserRoleList(userNo);
        if (userRoleList.isEmpty()) {
            throw  new ApplicationException(ErrorCode.NOT_FOUND_AUTH_INFO);
        }
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken(jwtUtils.createAccessToken(userNo, userRoleList))
                .refreshToken(jwtUtils.createRefreshToken(userNo))
                .build();
        this.saveRefreshToken(userNo, authCreateTokenVo.refreshToken());

        return authCreateTokenVo;
    }

    /**
     * 인증에 필요한 액세스 토큰과 리프레시 토큰을 발급하고, 반환한다.
     * 리프레시 토큰을 매개변수로 전달받는다.
     *
     * @param refreshToken 새롭게 액세스토큰을 생성할 리프레시 토큰
     * @return 액세스 토큰과 리프레시 토큰
     */
    public AuthRefreshTokenVo refreshToken(String refreshToken) {
        Jws<Claims> claimsJws = jwtUtils.parsingToken(refreshToken);
        Date expirationDate = claimsJws.getPayload().getExpiration();
        String userNo = (String) claimsJws.getPayload().get(StaticValues.USER_NO);
        List<String> userRoleList = this.getUserRoleList(userNo);

        AuthRefreshTokenVo authRefreshTokenVo = AuthRefreshTokenVo.builder()
                .accessToken(jwtUtils.createAccessToken(userNo, userRoleList))
                .refreshToken(jwtUtils.createRefreshToken(userNo, expirationDate))
                .build();
        this.saveRefreshToken(userNo, authRefreshTokenVo.refreshToken());

        return authRefreshTokenVo;
    }

    /**
     * 리프레시 토큰을 저장한다.
     *
     * @param userNo       회원 번호
     * @param refreshToken 만들어진 리프레시 토큰
     */
    private void saveRefreshToken(String userNo, String refreshToken) {
        Auth auth = authRepository
                .findById(userNo)
                .orElse(new Auth(userNo, Duration.ofMinutes(refreshKeyTtl)))
                ;
        auth.changeRefreshToken(refreshToken);
        authRepository.save(auth);
    }

    /**
     * 회원 번호와 대응되는 권한 리스트를 가져온다.
     *
     * @param userNo 회원 번호
     * @return 해당 회원이 가지고 있는 권한 리스트
     */
    private List<String> getUserRoleList(String userNo) {
        List<UserRole> userRoleList = userRoleRepository.findByUserRoleIdUserNo(userNo);
        List<Integer> targetRoleIdList = userRoleList.stream()
                .map(userRole -> userRole.getRole().getId())
                .toList();
        List<Role> roleList = roleRepository.findAllByIdIn(targetRoleIdList);
        return roleList.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
