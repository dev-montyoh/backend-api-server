package dev.montyoh.user.infrastructure.repository.feign.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.vo.AuthCreateTokenVo;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateTokenMapper {

    public abstract AuthCreateTokenReqDto mapToReqDto(String userNo);

    public abstract AuthCreateTokenVo mapToVo(AuthCreateTokenRspDto authCreateTokenRspDto);
}
