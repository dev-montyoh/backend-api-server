package dev.montyoh.user.interfaces.rest.controller;

import dev.montyoh.user.application.commandservice.UserMcpTokenCommandService;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserMcpTokenCommandMapper;
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
@Tag(name = "MCP Token API", description = "MCP 토큰 관련 API")
public class McpTokenController {

    private final UserMcpTokenCommandService userMcpTokenCommandService;

    private final UserMcpTokenCommandMapper userMcpTokenCommandMapper;

    /**
     * MCP 토큰 발급
     * loginId, password 를 검증한 후 MCP 전용 토큰을 응답 값으로 전달받는다.
     *
     * @param userMcpTokenReqDto MCP 토큰 발급 요청 Dto
     * @return MCP 토큰
     */
    @Operation(summary = "MCP 토큰 발급 API", description = "loginId/password 검증 후 MCP 전용 토큰을 발급한다.")
    @PostMapping(value = UserApiUrl.Mcp.MCP_TOKEN_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserMcpTokenRspDto> getMcpToken(@RequestBody UserMcpTokenReqDto userMcpTokenReqDto) {
        UserLoginCommand userLoginCommand = userMcpTokenCommandMapper.mapToCommand(userMcpTokenReqDto);
        AuthCreateMcpTokenVo authCreateMcpTokenVo = userMcpTokenCommandService.getMcpToken(userLoginCommand);
        UserMcpTokenRspDto userMcpTokenRspDto = userMcpTokenCommandMapper.mapToRspDto(authCreateMcpTokenVo);
        return new ResponseEntity<>(userMcpTokenRspDto, HttpStatus.OK);
    }
}
