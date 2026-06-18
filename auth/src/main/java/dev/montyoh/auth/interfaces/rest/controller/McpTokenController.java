package dev.montyoh.auth.interfaces.rest.controller;

import dev.montyoh.auth.application.commandservice.AuthMcpTokenCommandService;
import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.interfaces.rest.constants.AuthApiUrl;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenRspDto;
import dev.montyoh.auth.interfaces.rest.mapper.AuthCreateMcpTokenCommandMapper;
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
@RequestMapping(AuthApiUrl.AUTH_V1_BASE_URL)
@Tag(name = "MCP Token API", description = "MCP 토큰 관련 API")
public class McpTokenController {

    private final AuthMcpTokenCommandService authMcpTokenCommandService;

    private final AuthCreateMcpTokenCommandMapper authCreateMcpTokenCommandMapper;

    /**
     * MCP 토큰 발급 API
     * userNo 를 전달받아 MCP 전용 장기 토큰을 발급한다.
     *
     * @param authCreateMcpTokenReqDto MCP 토큰 발급 요청 Dto
     * @return MCP 토큰
     */
    @Operation(summary = "MCP 토큰 발급 API", description = "userNo 를 받아 MCP 전용 장기 토큰을 발급한다.")
    @PostMapping(value = AuthApiUrl.Mcp.CREATE_MCP_TOKEN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthCreateMcpTokenRspDto> createMcpToken(@RequestBody AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto) {
        AuthCreateMcpTokenCommand command = authCreateMcpTokenCommandMapper.mapToCommand(authCreateMcpTokenReqDto);
        AuthCreateMcpTokenVo authCreateMcpTokenVo = authMcpTokenCommandService.createMcpToken(command);
        AuthCreateMcpTokenRspDto authCreateMcpTokenRspDto = authCreateMcpTokenCommandMapper.mapToRspDto(authCreateMcpTokenVo);
        return new ResponseEntity<>(authCreateMcpTokenRspDto, HttpStatus.OK);
    }
}
