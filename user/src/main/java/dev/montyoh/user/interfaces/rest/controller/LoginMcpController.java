package dev.montyoh.user.interfaces.rest.controller;

import dev.montyoh.user.application.commandservice.UserMcpLoginCommandService;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserMcpLoginCommandMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserApiUrl.USER_V1_BASE_URL)
@Tag(name = "MCP Login API", description = "MCP 로그인 관련 API")
public class LoginMcpController {

    private final UserMcpLoginCommandService userMcpLoginCommandService;

    private final UserMcpLoginCommandMapper userMcpLoginCommandMapper;

    /**
     * MCP 로그인 API
     * loginId, password 를 검증한 후 MCP 전용 토큰을 발급한다.
     *
     * @param userMcpLoginReqDto MCP 로그인 요청 Dto
     * @return MCP 토큰
     */
    @Operation(summary = "MCP 로그인 API", description = "loginId/password 검증 후 MCP 전용 토큰을 발급한다.")
    @PostMapping(value = UserApiUrl.Login.USER_MCP_LOGIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserMcpLoginRspDto> login(@RequestBody UserMcpLoginReqDto userMcpLoginReqDto) {
        UserLoginCommand userLoginCommand = userMcpLoginCommandMapper.mapToCommand(userMcpLoginReqDto);
        AuthCreateMcpTokenVo authCreateMcpTokenVo = userMcpLoginCommandService.login(userLoginCommand);
        UserMcpLoginRspDto userMcpLoginRspDto = userMcpLoginCommandMapper.mapToRspDto(authCreateMcpTokenVo);
        return new ResponseEntity<>(userMcpLoginRspDto, HttpStatus.OK);
    }
}
