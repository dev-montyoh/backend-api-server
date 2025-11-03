package io.github.monty.api.auth.interfaces.rest.mapper;

import io.github.monty.api.auth.domain.model.command.AuthCreateTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthCreateTokenCommandMapperTest {
    private final AuthCreateTokenCommandMapper mapper = Mappers.getMapper(AuthCreateTokenCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        AuthCreateTokenReqDto authCreateTokenReqDto = AuthCreateTokenReqDto.builder()
                .userNo("testUserNo")
                .build();

        //  when
        AuthCreateTokenCommand actual = mapper.mapToCommand(authCreateTokenReqDto);

        //  then
        assertAll(
                () -> assertThat(actual.getUserNo()).isEqualTo(authCreateTokenReqDto.userNo())
        );
    }

    @Test
    void mapToRspDto() {
        //  given
        AuthCreateTokenVo authCreateTokenVo = AuthCreateTokenVo.builder()
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();

        //  when
        AuthCreateTokenRspDto actual = mapper.mapToRspDto(authCreateTokenVo);

        //  then
        assertAll(
                () -> assertThat(actual.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(actual.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken())
        );
    }
}