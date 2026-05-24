package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserLoginRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class UserLoginCommandMapper {

    public abstract UserLoginCommand mapToCommand(UserLoginReqDto userLoginReqDto);

    public abstract UserLoginRspDto mapToRspDto(AuthCreateTokenVo authCreateTokenVo);
}
