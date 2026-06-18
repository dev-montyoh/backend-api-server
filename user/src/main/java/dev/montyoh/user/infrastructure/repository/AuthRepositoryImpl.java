package dev.montyoh.user.infrastructure.repository;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.user.domain.repository.AuthRepository;
import dev.montyoh.user.infrastructure.repository.feign.AuthFeignClient;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenRspDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import dev.montyoh.user.infrastructure.repository.feign.mapper.AuthCreateMcpTokenMapper;
import dev.montyoh.user.infrastructure.repository.feign.mapper.AuthCreateTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthFeignClient authFeignClient;
    private final AuthCreateTokenMapper authCreateTokenMapper;
    private final AuthCreateMcpTokenMapper authCreateMcpTokenMapper;

    /**
     * Auth 애플리케이션에게 토큰 생성 요청을 한다.
     *
     * @param userNo 로그인 요청 대상
     * @return 토큰 생성 결과
     */
    @Override
    public AuthCreateTokenVo createAccessTokenAndRefreshToken(String userNo) {
        AuthCreateTokenReqDto authCreateTokenReqDto = authCreateTokenMapper.mapToReqDto(userNo);
        ResponseEntity<AuthCreateTokenRspDto> authCreateTokenRspDtoResponseEntity = authFeignClient.createAccessTokenAndRefreshToken(authCreateTokenReqDto);
        if (Objects.isNull(authCreateTokenRspDtoResponseEntity.getBody())) {
            throw new ApplicationException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
        return authCreateTokenMapper.mapToVo(authCreateTokenRspDtoResponseEntity.getBody());
    }

    /**
     * Auth 애플리케이션에게 MCP 토큰 생성 요청을 한다.
     *
     * @param userNo 로그인 요청 대상
     * @return MCP 토큰 생성 결과
     */
    @Override
    public AuthCreateMcpTokenVo createMcpToken(String userNo) {
        AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto = authCreateMcpTokenMapper.mapToReqDto(userNo);
        ResponseEntity<AuthCreateMcpTokenRspDto> response = authFeignClient.createMcpToken(authCreateMcpTokenReqDto);
        if (Objects.isNull(response.getBody())) {
            throw new ApplicationException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
        return authCreateMcpTokenMapper.mapToVo(response.getBody());
    }
}
