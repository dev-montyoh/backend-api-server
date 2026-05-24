package dev.montyoh.auth.interfaces.rest.mapper;

import dev.montyoh.auth.common.configuration.MapStructConfig;
import dev.montyoh.auth.domain.model.command.AuthCreateTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateTokenReqDto;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateTokenCommandMapper {

    public abstract AuthCreateTokenCommand mapToCommand(AuthCreateTokenReqDto authCreateTokenReqDto);

    public abstract AuthCreateTokenRspDto mapToRspDto(AuthCreateTokenVo authCreateTokenVo);
}
