package io.github.monty.api.user.interfaces.rest.mapper;

import io.github.monty.api.user.domain.model.command.MemberLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberLoginCommandMapperTest {
    private final MemberLoginCommandMapper memberLoginCommandMapper = Mappers.getMapper(MemberLoginCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        MemberLoginReqDto memberLoginReqDto = MemberLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        MemberLoginCommand memberLoginCommand = memberLoginCommandMapper.mapToCommand(memberLoginReqDto);

        //  then
        assertAll(
                () -> assertThat(memberLoginCommand.getLoginId()).isEqualTo(memberLoginReqDto.loginId()),
                () -> assertThat(memberLoginCommand.getPassword()).isEqualTo(memberLoginReqDto.password())
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
        MemberLoginRspDto memberLoginRspDto = memberLoginCommandMapper.mapToRspDto(authCreateTokenVo);

        //  then
        assertAll(
                () -> assertThat(memberLoginRspDto.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(memberLoginRspDto.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken())
        );
    }
}