package dev.montyoh.auth.interfaces.rest.mapper;

import dev.montyoh.auth.common.configuration.MapStructConfig;
import dev.montyoh.auth.domain.model.command.AuthRefreshTokenCommand;
import dev.montyoh.auth.domain.model.vo.AuthRefreshTokenVo;
import dev.montyoh.auth.interfaces.rest.dto.AuthRefreshTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthRefreshTokenCommandMapper {

    public abstract AuthRefreshTokenCommand mapToCommand(String refreshToken);

    public abstract AuthRefreshTokenRspDto mapToRspDto(AuthRefreshTokenVo authRefreshTokenVo);
}
