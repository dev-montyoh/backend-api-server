package io.github.monty.api.user.interfaces.rest.mapper;

import io.github.monty.api.user.common.configuration.MapStructConfig;
import io.github.monty.api.user.domain.model.command.UserLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.UserLoginRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class UserLoginCommandMapper {

    public abstract UserLoginCommand mapToCommand(UserLoginReqDto userLoginReqDto);

    public abstract UserLoginRspDto mapToRspDto(AuthCreateTokenVo authCreateTokenVo);
}
