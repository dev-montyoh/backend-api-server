package dev.montyoh.user.infrastructure.repository.feign.mapper;

import dev.montyoh.user.domain.model.vo.AuthCreateMcpTokenVo;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenReqDto;
import dev.montyoh.user.infrastructure.repository.feign.dto.AuthCreateMcpTokenRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class AuthCreateMcpTokenMapperTest {

    private final AuthCreateMcpTokenMapper mapper = Mappers.getMapper(AuthCreateMcpTokenMapper.class);

    @Test
    void mapToReqDto() {
        //  given
        String userNo = "testUserNo";

        //  when
        AuthCreateMcpTokenReqDto actual = mapper.mapToReqDto(userNo);

        //  then
        assertThat(actual.userNo()).isEqualTo(userNo);
    }

    @Test
    void mapToVo() {
        //  given
        AuthCreateMcpTokenRspDto authCreateMcpTokenRspDto = AuthCreateMcpTokenRspDto.builder()
                .token("testMcpToken")
                .build();

        //  when
        AuthCreateMcpTokenVo actual = mapper.mapToVo(authCreateMcpTokenRspDto);

        //  then
        assertThat(actual.token()).isEqualTo(authCreateMcpTokenRspDto.token());
    }
}
