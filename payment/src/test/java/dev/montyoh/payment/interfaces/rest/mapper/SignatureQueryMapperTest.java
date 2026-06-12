package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.query.InicisPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.query.NicepayPaymentSignatureQuery;
import dev.montyoh.payment.domain.model.vo.InicisPaymentSignatureResVo;
import dev.montyoh.payment.domain.model.vo.NicepayPaymentSignatureResVo;
import dev.montyoh.payment.interfaces.rest.dto.InicisPaymentSignatureResDto;
import dev.montyoh.payment.interfaces.rest.dto.NicepayPaymentSignatureResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SignatureQueryMapperTest {

    private final InicisPaymentSignatureQueryMapper inicisMapper =
            Mappers.getMapper(InicisPaymentSignatureQueryMapper.class);

    private final NicepayPaymentSignatureQueryMapper nicepayMapper =
            Mappers.getMapper(NicepayPaymentSignatureQueryMapper.class);

    @Test
    @DisplayName("이니시스 결제 인증 쿼리를 생성한다.")
    void inicis_mapToQuery() {
        //  when
        InicisPaymentSignatureQuery actual = inicisMapper.mapToQuery("testOid", "10000", PgProviderType.INICIS);

        //  then
        assertAll(
                () -> assertThat(actual.getOid()).isEqualTo("testOid"),
                () -> assertThat(actual.getPrice()).isEqualTo("10000"),
                () -> assertThat(actual.getPgProviderType()).isEqualTo(PgProviderType.INICIS)
        );
    }

    @Test
    @DisplayName("이니시스 결제 인증 응답 VO를 DTO로 변환한다.")
    void inicis_mapToDTO() {
        //  given
        InicisPaymentSignatureResVo resVo = InicisPaymentSignatureResVo.builder()
                .signature("testSignature")
                .verification("testVerification")
                .mKey("testMKey")
                .mid("testMid")
                .timestamp(12345678L)
                .build();

        //  when
        InicisPaymentSignatureResDto actual = inicisMapper.mapToDTO(resVo);

        //  then
        assertAll(
                () -> assertThat(actual.signature()).isEqualTo("testSignature"),
                () -> assertThat(actual.verification()).isEqualTo("testVerification"),
                () -> assertThat(actual.mKey()).isEqualTo("testMKey"),
                () -> assertThat(actual.mid()).isEqualTo("testMid")
        );
    }

    @Test
    @DisplayName("나이스페이 결제 인증 쿼리를 생성한다.")
    void nicepay_mapToQuery() {
        //  when
        NicepayPaymentSignatureQuery actual = nicepayMapper.mapToQuery("10000", PgProviderType.NICEPAY);

        //  then
        assertAll(
                () -> assertThat(actual.getPrice()).isEqualTo("10000"),
                () -> assertThat(actual.getPgProviderType()).isEqualTo(PgProviderType.NICEPAY)
        );
    }

    @Test
    @DisplayName("나이스페이 결제 인증 응답 VO를 DTO로 변환한다.")
    void nicepay_mapToDto() {
        //  given
        NicepayPaymentSignatureResVo resVo = NicepayPaymentSignatureResVo.builder()
                .mid("testMid")
                .ediDate("20260612120000")
                .signData("testSignData")
                .build();

        //  when
        NicepayPaymentSignatureResDto actual = nicepayMapper.mapToDto(resVo);

        //  then
        assertAll(
                () -> assertThat(actual.mid()).isEqualTo("testMid"),
                () -> assertThat(actual.ediDate()).isEqualTo("20260612120000"),
                () -> assertThat(actual.signData()).isEqualTo("testSignData")
        );
    }
}
