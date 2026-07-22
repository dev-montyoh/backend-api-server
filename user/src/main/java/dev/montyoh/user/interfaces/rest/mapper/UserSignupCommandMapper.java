package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.interfaces.rest.dto.UserSignupReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserSignupRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class UserSignupCommandMapper {

    public abstract UserCreateCommand mapToCommand(UserSignupReqDto userSignupReqDto);

    public abstract UserSignupRspDto mapToRspDto(User user);
}
