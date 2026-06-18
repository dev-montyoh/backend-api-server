package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class UserMcpTokenCommandMapper {

    public abstract UserLoginCommand mapToCommand(UserMcpTokenReqDto userMcpTokenReqDto);

    public abstract UserMcpTokenRspDto mapToRspDto(AuthCreateMcpTokenVo authCreateMcpTokenVo);
}
