package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserMcpLoginCommandMapperTest {

    private final UserMcpLoginCommandMapper userMcpLoginCommandMapper = Mappers.getMapper(UserMcpLoginCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        UserMcpLoginReqDto userMcpLoginReqDto = UserMcpLoginReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        UserLoginCommand userLoginCommand = userMcpLoginCommandMapper.mapToCommand(userMcpLoginReqDto);

        //  then
        assertAll(
                () -> assertThat(userLoginCommand.getLoginId()).isEqualTo(userMcpLoginReqDto.loginId()),
                () -> assertThat(userLoginCommand.getPassword()).isEqualTo(userMcpLoginReqDto.password())
        );
    }

    @Test
    void mapToRspDto() {
        //  given
        AuthCreateMcpTokenVo authCreateMcpTokenVo = AuthCreateMcpTokenVo.builder()
                .token("testMcpToken")
                .build();

        //  when
        UserMcpLoginRspDto userMcpLoginRspDto = userMcpLoginCommandMapper.mapToRspDto(authCreateMcpTokenVo);

        //  then
        assertThat(userMcpLoginRspDto.token()).isEqualTo(authCreateMcpTokenVo.token());
    }
}
