package dev.montyoh.auth.interfaces.rest.mapper;

import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthCreateMcpTokenCommandMapperTest {

    private final AuthCreateMcpTokenCommandMapper mapper = Mappers.getMapper(AuthCreateMcpTokenCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto = AuthCreateMcpTokenReqDto.builder()
                .userNo("testUserNo")
                .build();

        //  when
        AuthCreateMcpTokenCommand actual = mapper.mapToCommand(authCreateMcpTokenReqDto);

        //  then
        assertThat(actual.getUserNo()).isEqualTo(authCreateMcpTokenReqDto.userNo());
    }

    @Test
    void mapToRspDto() {
        //  given
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();

        //  when
        AuthCreateMcpTokenRspDto actual = mapper.mapToRspDto(authCreateMcpTokenVo);

        //  then
        assertAll(
                () -> assertThat(actual.token()).isEqualTo(authCreateMcpTokenVo.token())
        );
    }
}
