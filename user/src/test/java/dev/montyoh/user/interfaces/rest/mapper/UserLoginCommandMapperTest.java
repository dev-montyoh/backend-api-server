package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserLoginRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserLoginCommandMapperTest {
    private final UserLoginCommandMapper userLoginCommandMapper = Mappers.getMapper(UserLoginCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        UserLoginReqDto userLoginReqDto = UserLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        UserLoginCommand userLoginCommand = userLoginCommandMapper.mapToCommand(userLoginReqDto);

        //  then
        assertAll(
                () -> assertThat(userLoginCommand.getLoginId()).isEqualTo(userLoginReqDto.loginId()),
                () -> assertThat(userLoginCommand.getPassword()).isEqualTo(userLoginReqDto.password())
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
        UserLoginRspDto userLoginRspDto = userLoginCommandMapper.mapToRspDto(authCreateTokenVo);

        //  then
        assertAll(
                () -> assertThat(userLoginRspDto.accessToken()).isEqualTo(authCreateTokenVo.accessToken()),
                () -> assertThat(userLoginRspDto.refreshToken()).isEqualTo(authCreateTokenVo.refreshToken())
        );
    }
}