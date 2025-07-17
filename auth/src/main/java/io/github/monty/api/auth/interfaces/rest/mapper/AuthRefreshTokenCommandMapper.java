package io.github.monty.api.auth.interfaces.rest.mapper;

import io.github.monty.api.auth.common.configuration.MapStructConfig;
import io.github.monty.api.auth.domain.model.command.AuthRefreshTokenCommand;
import io.github.monty.api.auth.domain.model.vo.AuthRefreshTokenVo;
import io.github.monty.api.auth.interfaces.rest.dto.AuthRefreshTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthRefreshTokenCommandMapper {

    public abstract AuthRefreshTokenCommand mapToCommand(String refreshToken);

    public abstract AuthRefreshTokenRspDto mapToRspDto(AuthRefreshTokenVo authRefreshTokenVo);
}
