package dev.montyoh.payment.infrastructure.webclient.mapper;

import dev.montyoh.payment.domain.model.vo.*;
import dev.montyoh.payment.infrastructure.webclient.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class WebClientMapperTest {

    private final InicisPaymentApprovalMapper inicisApprovalMapper = Mappers.getMapper(InicisPaymentApprovalMapper.class);
    private final InicisPaymentCancelMapper iniCancelMapper = Mappers.getMapper(InicisPaymentCancelMapper.class);
    private final InicisPaymentNetworkCancelMapper inicisNetworkCancelMapper = Mappers.getMapper(InicisPaymentNetworkCancelMapper.class);
    private final NicepayPaymentApprovalMapper nicepayApprovalMapper = Mappers.getMapper(NicepayPaymentApprovalMapper.class);
    private final NicepayPaymentCancelMapper nicepayCancelMapper = Mappers.getMapper(NicepayPaymentCancelMapper.class);
    private final NicepayPaymentNetworkCancelMapper nicepayNetworkCancelMapper = Mappers.getMapper(NicepayPaymentNetworkCancelMapper.class);

    // === Inicis Approval Mapper ===

    @Test
    @DisplayName("InicisPaymentApprovalMapper - 날짜/시간이 있으면 approvalDateTime 이 파싱된다.")
    void inicisApproval_mapToVo_withDateTime() {
        InicisPaymentApprovalResponse response = InicisPaymentApprovalResponse.builder()
                .resultCode("0000").resultMsg("정상 처리").tid("testTid").mid("testMid")
                .totPrice(10000L).payMethod("CARD").applDate("20260612").applTime("120000")
                .buyerTel("010-1234-5678").buyerEmail("test@test.com")
                .build();

        InicisPaymentApprovalResVo vo = inicisApprovalMapper.mapToVo(response);

        assertAll(
                () -> assertThat(vo.getTid()).isEqualTo("testTid"),
                () -> assertThat(vo.getResultMessage()).isEqualTo("정상 처리"),
                () -> assertThat(vo.getAmount()).isEqualTo("10000"),
                () -> assertThat(vo.getPaymentMethod()).isEqualTo("CARD"),
                () -> assertThat(vo.getBuyerPhoneNumber()).isEqualTo("010-1234-5678"),
                () -> assertThat(vo.getBuyerEmail()).isEqualTo("test@test.com"),
                () -> assertThat(vo.getApprovalDateTime()).isEqualTo(LocalDateTime.of(2026, 6, 12, 12, 0, 0))
        );
    }

    @Test
    @DisplayName("InicisPaymentApprovalMapper - 날짜/시간이 없으면 approvalDateTime 이 null 이다.")
    void inicisApproval_mapToVo_withoutDateTime() {
        InicisPaymentApprovalResponse response = InicisPaymentApprovalResponse.builder()
                .resultCode("0000").resultMsg("정상 처리").tid("testTid").totPrice(5000L)
                .build();

        InicisPaymentApprovalResVo vo = inicisApprovalMapper.mapToVo(response);

        assertThat(vo.getApprovalDateTime()).isNull();
    }

    // === Inicis Cancel Mapper ===

    @Test
    @DisplayName("InicisPaymentCancelMapper - 취소 결과를 올바르게 매핑한다.")
    void inicisCancel_mapToVo() {
        InicisPaymentCancelResponse response = InicisPaymentCancelResponse.builder()
                .resultCode("00").resultMsg("취소 성공").cancelDate("20260612").cancelTime("120000")
                .cshrCancelNum("cashReceiptNo123")
                .build();

        InicisPaymentCancelResVo vo = iniCancelMapper.mapToVo(response, "취소 사유");

        assertAll(
                () -> assertThat(vo.getResultMessage()).isEqualTo("취소 성공"),
                () -> assertThat(vo.getReason()).isEqualTo("취소 사유"),
                () -> assertThat(vo.getCashReceiptCancelNo()).isEqualTo("cashReceiptNo123"),
                () -> assertThat(vo.getCancelDateTime()).isEqualTo(LocalDateTime.of(2026, 6, 12, 12, 0, 0))
        );
    }

    // === Inicis Network Cancel Mapper ===

    @Test
    @DisplayName("InicisPaymentNetworkCancelMapper - 망취소 결과를 올바르게 매핑한다.")
    void inicisNetworkCancel_mapToVo() {
        InicisPaymentNetworkCancelResponse response = InicisPaymentNetworkCancelResponse.builder()
                .resultCode("0000").resultMsg("망취소 성공").tid("testTid")
                .build();

        InicisPaymentNetworkCancelResVo vo = inicisNetworkCancelMapper.mapToVo(response);

        assertAll(
                () -> assertThat(vo.getResultMessage()).isEqualTo("망취소 성공"),
                () -> assertThat(vo.getTid()).isEqualTo("testTid")
        );
    }

    // === Nicepay Approval Mapper ===

    @Test
    @DisplayName("NicepayPaymentApprovalMapper.mapToDto() - 요청 VO 를 DTO 로 매핑한다.")
    void nicepayApproval_mapToDto() {
        NicepayPaymentApprovalReqVo reqVo = NicepayPaymentApprovalReqVo.builder()
                .transactionId("testTxId").authToken("testAuthToken").mid("testMid")
                .amount("10000").ediDate("20260612120000").signData("testSignData")
                .mallReserved("").build();

        NicepayPaymentApprovalRequest dto = nicepayApprovalMapper.mapToDto(reqVo, "JSON", "utf-8");

        assertAll(
                () -> assertThat(dto.getTID()).isEqualTo("testTxId"),
                () -> assertThat(dto.getAuthToken()).isEqualTo("testAuthToken"),
                () -> assertThat(dto.getMID()).isEqualTo("testMid"),
                () -> assertThat(dto.getAmt()).isEqualTo("10000"),
                () -> assertThat(dto.getEdiDate()).isEqualTo("20260612120000"),
                () -> assertThat(dto.getSignData()).isEqualTo("testSignData"),
                () -> assertThat(dto.getEdiType()).isEqualTo("JSON"),
                () -> assertThat(dto.getMallReserved()).isEqualTo("")
        );
    }

    @Test
    @DisplayName("NicepayPaymentApprovalMapper.mapToVo() - 승인 결과를 올바르게 매핑한다.")
    void nicepayApproval_mapToVo() {
        NicepayPaymentApprovalResponse response = NicepayPaymentApprovalResponse.builder()
                .resultCode("3001").resultMsg("성공").tid("testTid").amt("10000")
                .authDate("260612120000").buyerTel("010-1234-5678").buyerEmail("test@test.com")
                .payMethod("CARD").signature("testSignature")
                .build();

        NicepayPaymentApprovalResVo vo = nicepayApprovalMapper.mapToVo(response);

        assertAll(
                () -> assertThat(vo.getTid()).isEqualTo("testTid"),
                () -> assertThat(vo.getResultMessage()).isEqualTo("성공"),
                () -> assertThat(vo.getAmount()).isEqualTo("10000"),
                () -> assertThat(vo.getBuyerPhoneNumber()).isEqualTo("010-1234-5678"),
                () -> assertThat(vo.getBuyerEmail()).isEqualTo("test@test.com"),
                () -> assertThat(vo.getPaymentMethod()).isEqualTo("CARD"),
                () -> assertThat(vo.getSignature()).isEqualTo("testSignature"),
                () -> assertThat(vo.getApprovalDateTime()).isEqualTo(LocalDateTime.of(2026, 6, 12, 12, 0, 0))
        );
    }

    // === Nicepay Cancel Mapper ===

    @Test
    @DisplayName("NicepayPaymentCancelMapper.mapToDto() - 취소 요청 VO 를 DTO 로 매핑한다.")
    void nicepayCancel_mapToDto() {
        NicepayPaymentCancelReqVo reqVo = new NicepayPaymentCancelReqVo(
                "testTxId", "testMid", "testOrderNo", 10000L, "취소 사유",
                "20260612120000", "testSignData", ""
        );

        NicepayPaymentCancelRequest dto = nicepayCancelMapper.mapToDto(reqVo, "JSON", "utf-8");

        assertAll(
                () -> assertThat(dto.getTID()).isEqualTo("testTxId"),
                () -> assertThat(dto.getMID()).isEqualTo("testMid"),
                () -> assertThat(dto.getMoid()).isEqualTo("testOrderNo"),
                () -> assertThat(dto.getCancelAmt()).isEqualTo("10000"),
                () -> assertThat(dto.getCancelMsg()).isEqualTo("취소 사유"),
                () -> assertThat(dto.getEdiDate()).isEqualTo("20260612120000"),
                () -> assertThat(dto.getSignData()).isEqualTo("testSignData"),
                () -> assertThat(dto.getPartialCancelCode()).isEqualTo("0"),
                () -> assertThat(dto.getEdiType()).isEqualTo("JSON"),
                () -> assertThat(dto.getCharSet()).isEqualTo("utf-8"),
                () -> assertThat(dto.getMallReserved()).isEqualTo("")
        );
    }

    @Test
    @DisplayName("NicepayPaymentCancelMapper.mapToVo() - 취소일시가 있으면 cancelDateTime 이 설정된다.")
    void nicepayCancel_mapToVo_withDateTime() {
        NicepayPaymentCancelResponse response = NicepayPaymentCancelResponse.builder()
                .resultCode("2001").resultMsg("취소 성공").tid("testTid").cancelAmt("10000")
                .signature("testSignature").cancelDate("20260612").cancelTime("120000")
                .build();

        NicepayPaymentCancelResVo vo = nicepayCancelMapper.mapToVo(response, "취소 사유");

        assertAll(
                () -> assertThat(vo.getResultMessage()).isEqualTo("취소 성공"),
                () -> assertThat(vo.getReason()).isEqualTo("취소 사유"),
                () -> assertThat(vo.getCancelAmount()).isEqualTo("10000"),
                () -> assertThat(vo.getSignature()).isEqualTo("testSignature"),
                () -> assertThat(vo.getCancelTransactionId()).isEqualTo("testTid"),
                () -> assertThat(vo.getCancelDateTime()).isEqualTo(LocalDateTime.of(2026, 6, 12, 12, 0, 0))
        );
    }

    @Test
    @DisplayName("NicepayPaymentCancelMapper.mapToVo() - 취소일시가 없으면 cancelDateTime 이 null 이다.")
    void nicepayCancel_mapToVo_withoutDateTime() {
        NicepayPaymentCancelResponse response = NicepayPaymentCancelResponse.builder()
                .resultCode("2001").resultMsg("취소 성공")
                .build();

        NicepayPaymentCancelResVo vo = nicepayCancelMapper.mapToVo(response, "취소 사유");

        assertThat(vo.getCancelDateTime()).isNull();
    }

    // === Nicepay Network Cancel Mapper ===

    @Test
    @DisplayName("NicepayPaymentNetworkCancelMapper.mapToDto() - 망취소 요청 VO 를 DTO 로 매핑한다.")
    void nicepayNetworkCancel_mapToDto() {
        NicepayPaymentNetworkCancelReqVo reqVo = new NicepayPaymentNetworkCancelReqVo(
                "testTxId", "testAuthToken", "testMid", 10000L, "20260612120000",
                "testSignData", "", "http://network-cancel.url", "testOrderNo"
        );

        NicepayPaymentNetworkCancelRequest dto = nicepayNetworkCancelMapper.mapToDto(reqVo, "JSON", "utf-8", "망취소");

        assertAll(
                () -> assertThat(dto.getTID()).isEqualTo("testTxId"),
                () -> assertThat(dto.getAuthToken()).isEqualTo("testAuthToken"),
                () -> assertThat(dto.getMID()).isEqualTo("testMid"),
                () -> assertThat(dto.getAmt()).isEqualTo("10000"),
                () -> assertThat(dto.getEdiDate()).isEqualTo("20260612120000"),
                () -> assertThat(dto.getSignData()).isEqualTo("testSignData"),
                () -> assertThat(dto.getMallReserved()).isEqualTo(""),
                () -> assertThat(dto.getNetCancel()).isEqualTo("1"),
                () -> assertThat(dto.getEdiType()).isEqualTo("JSON"),
                () -> assertThat(dto.getCharSet()).isEqualTo("utf-8")
        );
    }

    @Test
    @DisplayName("NicepayPaymentNetworkCancelMapper.mapToVo() - 망취소 결과를 올바르게 매핑한다.")
    void nicepayNetworkCancel_mapToVo() {
        NicepayPaymentNetworkCancelResponse response = NicepayPaymentNetworkCancelResponse.builder()
                .resultCode("2001").resultMsg("망취소 성공").tid("testTid")
                .cancelAmt("10000").signature("testSignature")
                .build();

        NicepayPaymentNetworkCancelResVo vo = nicepayNetworkCancelMapper.mapToVo(response);

        assertAll(
                () -> assertThat(vo.getResultMessage()).isEqualTo("망취소 성공"),
                () -> assertThat(vo.getTid()).isEqualTo("testTid"),
                () -> assertThat(vo.getCancelAmount()).isEqualTo("10000"),
                () -> assertThat(vo.getSignature()).isEqualTo("testSignature")
        );
    }
}
