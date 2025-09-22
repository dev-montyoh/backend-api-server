package io.github.monty.api.user.infrastructure.repository;

import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.infrastructure.repository.feign.AuthFeignClient;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import io.github.monty.api.user.infrastructure.repository.feign.mapper.AuthCreateTokenMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthRepositoryImplTest {
    @InjectMocks
    private AuthRepositoryImpl authRepository;

    @Mock
    private AuthFeignClient authFeignClient;
    @Mock
    private AuthCreateTokenMapper authCreateTokenMapper;

    @Test
    @DisplayName("토큰 정보 생성 요청에 성공했다.")
    void createAccessTokenAndRefreshToken() {
        //  given
        AuthCreateTokenRspDto authCreateTokenRspDto = AuthCreateTokenRspDto.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();
        given(authFeignClient.createAccessTokenAndRefreshToken(any())).willReturn(ResponseEntity.of(Optional.of(authCreateTokenRspDto)));
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken(authCreateTokenRspDto.accessToken())
                .refreshToken(authCreateTokenRspDto.refreshToken())
                .build();
        given(authCreateTokenMapper.mapToVo(any())).willReturn(authCreateTokenVo);
        String userId = "testUserId";

        //  when
        AuthCreateTokenVo actual = authRepository.createAccessTokenAndRefreshToken(userId);

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(actual.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken())
        );
    }

    @Test
    @DisplayName("토큰 정보가 비정상적이다.")
    void getUserNoAndCreateToken_invalid_token_info() {
        //  given
        given(authFeignClient.createAccessTokenAndRefreshToken(any())).willReturn(ResponseEntity.of(Optional.empty()));

        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> authRepository.createAccessTokenAndRefreshToken("testLoginId"));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.EXTERNAL_SERVER_ERROR.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.EXTERNAL_SERVER_ERROR.getMessage())
        );
    }
}