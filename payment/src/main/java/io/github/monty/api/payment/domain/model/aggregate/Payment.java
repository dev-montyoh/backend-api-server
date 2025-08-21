package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentGatewayType;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.domain.model.vo.PaymentApprovalResultVO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
public class Payment {

    @Id
    @Size(max = 100)
    @Column(name = "payment_id", nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Size(max = 100)
    @NotNull
    @Column(name = "payment_no", nullable = false, length = 100)
    private String paymentNo;

    @Size(max = 100)
    @NotNull
    @Column(name = "order_no", nullable = false, length = 100)
    private String orderNo;

    @NotNull
    @Column(name = "pg_type", nullable = false, length = 50)
    private PaymentGatewayType paymentGatewayType;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @NotNull
    @Column(name = "requested_amount", nullable = false)
    private Long requestedAmount;

    @NotNull
    @Column(name = "approved_amount", nullable = false)
    private Long approvedAmount;

    @NotNull
    @Column(name = "refunded_amount", nullable = false)
    private Long refundedAmount;

    @Size(max = 20)
    @Column(name = "tid", length = 20)
    private String tid;

    @Size(max = 20)
    @Column(name = "buyer_phone_number", length = 20)
    private String buyerPhoneNumber;

    @Size(max = 50)
    @Column(name = "buyer_email", length = 50)
    private String buyerEmail;

    public void applyApprovePaymentResult(PaymentApprovalResultVO paymentApprovalResultVO) {
        this.tid = paymentApprovalResultVO.getTid();
        this.approvedAmount = paymentApprovalResultVO.getAmount();
        this.buyerPhoneNumber = paymentApprovalResultVO.getBuyerPhoneNumber();
        this.buyerEmail = paymentApprovalResultVO.getBuyerEmail();
    }
}