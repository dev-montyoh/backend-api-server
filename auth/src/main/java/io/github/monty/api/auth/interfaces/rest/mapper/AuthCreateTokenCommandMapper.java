package io.github.monty.api.auth.interfaces.rest.mapper;

import io.github.monty.api.auth.common.configuration.MapStructConfig;
import io.github.monty.api.auth.domain.model.command.AuthCreateTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import io.github.monty.api.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateTokenCommandMapper {

    public abstract AuthCreateTokenCommand mapToCommand(AuthCreateTokenReqDto authCreateTokenReqDto);

    public abstract AuthCreateTokenRspDto mapToRspDto(AuthCreateTokenVo authCreateTokenVo);
}
