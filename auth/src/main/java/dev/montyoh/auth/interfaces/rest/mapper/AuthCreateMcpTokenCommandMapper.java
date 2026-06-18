package dev.montyoh.auth.interfaces.rest.mapper;

import dev.montyoh.auth.common.configuration.MapStructConfig;
import dev.montyoh.auth.domain.model.command.AuthCreateMcpTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.auth.interfaces.rest.dto.AuthCreateMcpTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateMcpTokenCommandMapper {

    public abstract AuthCreateMcpTokenCommand mapToCommand(AuthCreateMcpTokenReqDto authCreateMcpTokenReqDto);

    public abstract AuthCreateMcpTokenRspDto mapToRspDto(AuthCreateMcpTokenVo authCreateMcpTokenVo);
}
