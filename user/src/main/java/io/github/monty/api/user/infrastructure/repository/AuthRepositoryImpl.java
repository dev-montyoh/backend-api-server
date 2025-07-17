package io.github.monty.api.user.infrastructure.repository;

import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.domain.repository.AuthRepository;
import io.github.monty.api.user.infrastructure.repository.feign.AuthFeignClient;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import io.github.monty.api.user.infrastructure.repository.feign.mapper.AuthCreateTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthFeignClient authFeignClient;
    private final AuthCreateTokenMapper authCreateTokenMapper;

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
}
