package io.github.monty.api.auth.interfaces.rest.controller;

import io.github.monty.api.auth.application.commandservice.AuthTokenCommandService;
import io.github.monty.api.auth.common.constants.CustomHeaders;
import io.github.monty.api.auth.domain.model.command.AuthCreateTokenCommand;
import io.github.monty.api.auth.domain.model.command.AuthRefreshTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.interfaces.rest.constants.AuthApiUrl;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthRefreshTokenRspDto;
import io.github.monty.api.auth.interfaces.rest.mapper.AuthCreateTokenCommandMapper;
import io.github.monty.api.auth.interfaces.rest.mapper.AuthRefreshTokenCommandMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthApiUrl.AUTH_V1_BASE_URL)
@Tag(name = "Auth Token API", description = "토큰 관련 API")
public class AuthTokenController {

    private final AuthTokenCommandService authTokenCommandService;

    private final AuthCreateTokenCommandMapper authCreateTokenCommandMapper;
    private final AuthRefreshTokenCommandMapper authRefreshTokenCommandMapper;

    /**
     * 토큰 생성 API
     *
     * @param authCreateTokenReqDto 토큰 생성 요청 Dto
     * @return 토큰 생성 결과
     */
    @Operation(summary = "토큰 생성 API", description = "엑세스 토큰, 리프레시 토큰을 생성하고 반환한다.")
    @PostMapping(value = AuthApiUrl.Token.CREATE_TOKEN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthCreateTokenRspDto> createToken(@RequestBody AuthCreateTokenReqDto authCreateTokenReqDto) {
        AuthCreateTokenCommand authCreateTokenCommand = authCreateTokenCommandMapper.mapToCommand(authCreateTokenReqDto);
        AuthCreateTokenVo authCreateTokenVo = authTokenCommandService.createToken(authCreateTokenCommand);
        AuthCreateTokenRspDto authCreateTokenRspDto = authCreateTokenCommandMapper.mapToRspDto(authCreateTokenVo);
        return new ResponseEntity<>(authCreateTokenRspDto, HttpStatus.OK);
    }

    /**
     * 토큰 갱신 API
     *
     * @param refreshToken 토큰 갱신에 필요한 refreshToken (Header)
     * @return 토큰 갱신 결과
     */
    @Operation(summary = "토큰 갱신 API", description = "리프레시 토큰을 갖고 토큰 갱신 후 액세스 토큰과 리프레시 토큰을 반환한다.")
    @PutMapping(value = AuthApiUrl.Token.REFRESH_TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthRefreshTokenRspDto> refreshToken(@RequestHeader(CustomHeaders.REFRESH_TOKEN) String refreshToken) {
        AuthRefreshTokenCommand authRefreshTokenCommand = authRefreshTokenCommandMapper.mapToCommand(refreshToken);
        AuthRefreshTokenVo authRefreshTokenVo = authTokenCommandService.refreshToken(authRefreshTokenCommand);
        AuthRefreshTokenRspDto authRefreshTokenRspDto = authRefreshTokenCommandMapper.mapToRspDto(authRefreshTokenVo);
        return new ResponseEntity<>(authRefreshTokenRspDto, HttpStatus.OK);
    }
}
