package dev.montyoh.payment.interfaces.rest.mapper;

import dev.montyoh.payment.domain.model.command.PaymentNetworkCancelCommand;
import dev.montyoh.payment.domain.model.query.PaymentListQuery;
import dev.montyoh.payment.domain.model.query.PaymentLogListQuery;
import dev.montyoh.payment.domain.model.vo.PaymentListResVo;
import dev.montyoh.payment.domain.model.vo.PaymentLogListResVo;
import dev.montyoh.payment.interfaces.rest.dto.PaymentListResDto;
import dev.montyoh.payment.interfaces.rest.dto.PaymentLogListResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentQueryMapperTest {

    private final PaymentListQueryMapper paymentListQueryMapper =
            Mappers.getMapper(PaymentListQueryMapper.class);

    private final PaymentLogListQueryMapper paymentLogListQueryMapper =
            Mappers.getMapper(PaymentLogListQueryMapper.class);

    private final PaymentNetworkCancelCommandMapper paymentNetworkCancelCommandMapper =
            Mappers.getMapper(PaymentNetworkCancelCommandMapper.class);

    @Test
    @DisplayName("결제 목록 조회 쿼리를 생성하고 pageable 이 설정된다.")
    void paymentList_mapToQuery() {
        //  when
        PaymentListQuery actual = paymentListQueryMapper.mapToQuery(1L, 50L);

        //  then
        assertAll(
                () -> assertThat(actual.pageable()).isNotNull(),
                () -> assertThat(actual.pageable().getPageNumber()).isEqualTo(0),
                () -> assertThat(actual.pageable().getPageSize()).isEqualTo(50)
        );
    }

    @Test
    @DisplayName("PaymentListResVo를 PaymentListResDto로 변환한다.")
    void paymentList_mapToDto() {
        //  given
        PaymentListResVo resVo = PaymentListResVo.builder()
                .paymentList(List.of()).totalPages(0L).totalCount(0L).build();

        //  when
        PaymentListResDto actual = paymentListQueryMapper.mapToDto(resVo);

        //  then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("결제 로그 목록 조회 쿼리를 생성하고 pageable 이 설정된다.")
    void paymentLogList_mapToQuery() {
        //  when
        PaymentLogListQuery actual = paymentLogListQueryMapper.mapToQuery("testPaymentNo", 1L, 50L);

        //  then
        assertAll(
                () -> assertThat(actual.paymentNo()).isEqualTo("testPaymentNo"),
                () -> assertThat(actual.pageable()).isNotNull(),
                () -> assertThat(actual.pageable().getPageNumber()).isEqualTo(0),
                () -> assertThat(actual.pageable().getPageSize()).isEqualTo(50)
        );
    }

    @Test
    @DisplayName("PaymentLogListResVo를 PaymentLogListResDto로 변환한다.")
    void paymentLogList_mapToDto() {
        //  given
        PaymentLogListResVo resVo = PaymentLogListResVo.builder()
                .paymentLogList(List.of()).totalPages(0L).totalCount(0L).build();

        //  when
        PaymentLogListResDto actual = paymentLogListQueryMapper.mapToDto(resVo);

        //  then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("결제번호를 PaymentNetworkCancelCommand로 변환한다.")
    void networkCancel_mapToCommand() {
        //  when
        PaymentNetworkCancelCommand actual = paymentNetworkCancelCommandMapper.mapToCommand("testPaymentNo");

        //  then
        assertThat(actual.getPaymentNo()).isEqualTo("testPaymentNo");
    }
}
