package io.github.monty.api.user.interfaces.rest.mapper;

import io.github.monty.api.user.common.configuration.MapStructConfig;
import io.github.monty.api.user.domain.model.command.MemberLoginCommand;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginReqDto;
import io.github.monty.api.user.interfaces.rest.dto.MemberLoginRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class MemberLoginCommandMapper {

    public abstract MemberLoginCommand mapToCommand(MemberLoginReqDto memberLoginReqDto);

    public abstract MemberLoginRspDto mapToRspDto(AuthCreateTokenVo authCreateTokenVo);
}
