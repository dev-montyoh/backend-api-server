package dev.montyoh.user.infrastructure.repository.feign.mapper;

import dev.montyoh.user.common.configuration.MapStructConfig;
import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenRspDto;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class AuthCreateMcpTokenMapper {

    public abstract AuthCreateMcpTokenReqDto mapToReqDto(String userNo);

    public abstract AuthCreateMcpTokenVo mapToVo(AuthCreateMcpTokenRspDto authCreateMcpTokenRspDto);
}
