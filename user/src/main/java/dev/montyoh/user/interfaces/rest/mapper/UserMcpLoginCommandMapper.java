package dev.montyoh.user.interfaces.rest.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.command.UserLoginCommand;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserMcpLoginRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class UserMcpLoginCommandMapper {

    public abstract UserLoginCommand mapToCommand(UserMcpLoginReqDto userMcpLoginReqDto);

    public abstract UserMcpLoginRspDto mapToRspDto(AuthCreateMcpTokenVo authCreateMcpTokenVo);
}
