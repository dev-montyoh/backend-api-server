package io.github.monty.api.user.infrastructure.repository.feign.mapper;

import io.github.monty.api.user.common.configuration.MapStructConfig;
import io.github.monty.api.user.domain.model.vo.AuthCreateTokenVo;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import io.github.monty.api.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateTokenMapper {

    public abstract AuthCreateTokenReqDto mapToReqDto(String userNo);

    public abstract AuthCreateTokenVo mapToVo(AuthCreateTokenRspDto authCreateTokenRspDto);
}
