package dev.montyoh.payment.infrastructure.querydsl;

import dev.montyoh.payment.common.configuration.JPAConfig;
import dev.montyoh.payment.infrastructure.querydsl.PaymentCustomRepositoryImpl;
import dev.montyoh.payment.infrastructure.querydsl.PaymentLogCustomRepositoryImpl;
import dev.montyoh.payment.common.constants.PaymentStatus;
import dev.montyoh.payment.common.constants.PgProviderType;
import dev.montyoh.payment.domain.model.aggregate.InicisPayment;
import dev.montyoh.payment.domain.model.command.InicisPaymentCreateCommand;
import dev.montyoh.payment.domain.model.entity.PaymentLog;
import dev.montyoh.payment.domain.repository.PaymentCustomRepository;
import dev.montyoh.payment.domain.repository.PaymentLogCustomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JPAConfig.class, PaymentCustomRepositoryImpl.class, PaymentLogCustomRepositoryImpl.class})
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS payment",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class PaymentQueryDSLRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentCustomRepository paymentCustomRepository;

    @Autowired
    private PaymentLogCustomRepository paymentLogCustomRepository;

    private InicisPayment persistPayment(String paymentNo) {
        InicisPaymentCreateCommand cmd = InicisPaymentCreateCommand.builder()
                .pgProviderType(PgProviderType.INICIS)
                .orderNo("testOrderNo")
                .price(10000L)
                .authToken("testAuthToken")
                .idcCode("testIdcCode")
                .approvalUrl("http://approval.url")
                .networkCancelUrl("http://network-cancel.url")
                .build();
        InicisPayment payment = new InicisPayment(paymentNo, cmd);

        entityManager.persist(payment);
        entityManager.flush();
        entityManager.clear();
        return payment;
    }

    @Test
    @DisplayName("findByPaymentNoWithCancelList - 결제번호로 결제를 조회한다.")
    void findByPaymentNoWithCancelList_found() {
        persistPayment("qdsPaymentNo1");

        Optional<dev.montyoh.payment.domain.model.aggregate.Payment> result =
                paymentCustomRepository.findByPaymentNoWithCancelList("qdsPaymentNo1");

        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getPaymentNo()).isEqualTo("qdsPaymentNo1"),
                () -> assertThat(result.get().getPaymentStatus()).isEqualTo(PaymentStatus.AUTHENTICATED)
        );
    }

    @Test
    @DisplayName("findByPaymentNoWithCancelList - 없는 결제번호이면 빈 Optional을 반환한다.")
    void findByPaymentNoWithCancelList_notFound() {
        Optional<dev.montyoh.payment.domain.model.aggregate.Payment> result =
                paymentCustomRepository.findByPaymentNoWithCancelList("nonExistentNo");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("PaymentLog findAll - 결제번호로 로그 목록을 조회한다.")
    void paymentLogFindAll_success() {
        persistPayment("qdsPaymentNo2");

        Page<PaymentLog> result = paymentLogCustomRepository.findAll("qdsPaymentNo2", PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).hasSize(1),
                () -> assertThat(result.getContent().get(0).getPaymentStatus()).isEqualTo(PaymentStatus.AUTHENTICATED),
                () -> assertThat(result.getTotalElements()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("PaymentLog findAll - 결과가 없으면 빈 Page를 반환한다.")
    void paymentLogFindAll_empty() {
        Page<PaymentLog> result = paymentLogCustomRepository.findAll("nonExistentNo", PageRequest.of(0, 10));

        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.getTotalElements()).isEqualTo(0)
        );
    }
}
