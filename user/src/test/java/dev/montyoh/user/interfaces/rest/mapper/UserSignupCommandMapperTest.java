package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.interfaces.rest.dto.UserSignupReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserSignupRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserSignupCommandMapperTest {
    private final UserSignupCommandMapper userSignupCommandMapper = Mappers.getMapper(UserSignupCommandMapper.class);

    @Test
    void mapToCommand() {
        //  given
        UserSignupReqDto userSignupReqDto = UserSignupReqDto.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        UserCreateCommand userCreateCommand = userSignupCommandMapper.mapToCommand(userSignupReqDto);

        //  then
        assertAll(
                () -> assertThat(userCreateCommand.getLoginId()).isEqualTo(userSignupReqDto.loginId()),
                () -> assertThat(userCreateCommand.getPassword()).isEqualTo(userSignupReqDto.password())
        );
    }

    @Test
    void mapToRspDto() {
        //  given
        User user = User.builder()
                .userNo("testUserNo")
                .loginId("testLoginId")
                .build();

        //  when
        UserSignupRspDto userSignupRspDto = userSignupCommandMapper.mapToRspDto(user);

        //  then
        assertThat(userSignupRspDto.userNo()).isEqualTo(user.getUserNo());
    }
}
