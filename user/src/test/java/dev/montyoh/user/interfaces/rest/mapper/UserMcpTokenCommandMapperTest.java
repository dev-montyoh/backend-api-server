package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMcpTokenCommandMapperTest {

    private final UserMcpTokenCommandMapper userMcpTokenCommandMapper = Mappers.getMapper(UserMcpTokenCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        UserMcpTokenReqDto userMcpTokenReqDto = UserMcpTokenReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        UserLoginCommand userLoginCommand = userMcpTokenCommandMapper.mapToCommand(userMcpTokenReqDto);

        //  then
        assertAll(
                () -> assertThat(userLoginCommand.getLoginId()).isEqualTo(userMcpTokenReqDto.loginId()),
                () -> assertThat(userLoginCommand.getPassword()).isEqualTo(userMcpTokenReqDto.password())
        );
    }

    @Test
    void mapToRspDto() {
        //  given
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();

        //  when
        UserMcpTokenRspDto userMcpTokenRspDto = userMcpTokenCommandMapper.mapToRspDto(authCreateMcpTokenVo);

        //  then
        assertThat(userMcpTokenRspDto.token()).isEqualTo(authCreateMcpTokenVo.token());
    }
}
