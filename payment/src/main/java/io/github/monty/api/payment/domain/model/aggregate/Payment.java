package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.entity.BaseEntity;
import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import io.github.monty.api.payment.domain.model.vo.PaymentApprovalResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentNetworkCancelResultVO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payment")
public class Payment extends BaseEntity {

    public Payment(String paymentNo, PaymentCreateCommand paymentCreateCommand) {
        this.paymentNo = paymentNo;
        this.requestAmount = paymentCreateCommand.getPrice();
        this.approvalAmount = 0L;
        this.refundAmount = 0L;
        this.orderNo = paymentCreateCommand.getOrderNo();
        this.paymentServiceProviderType = paymentCreateCommand.getPaymentServiceProviderType();
        this.paymentStatus = PaymentStatus.AUTHENTICATED;
    }

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
    @Column(name = "pg_provider_type", nullable = false, length = 50)
    private PaymentServiceProviderType paymentServiceProviderType;

    @NotNull
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @NotNull
    @Column(name = "request_amount", nullable = false)
    private Long requestAmount;

    @NotNull
    @Column(name = "approval_amount", nullable = false)
    private Long approvalAmount;

    @NotNull
    @Column(name = "refund_amount", nullable = false)
    private Long refundAmount;

    @Size(max = 100)
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Size(max = 20)
    @Column(name = "buyer_phone", length = 20)
    private String buyerPhone;

    @Size(max = 50)
    @Column(name = "buyer_email", length = 50)
    private String buyerEmail;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogList = new ArrayList<>();

    /**
     * 결제 승인 결과를 반영한다.
     *
     * @param paymentApprovalResultVO 승인 요청 결과 VO
     */
    public void applyPaymentApprovalResult(PaymentApprovalResultVO paymentApprovalResultVO) {
        this.transactionId = paymentApprovalResultVO.getTid();
        this.approvalAmount = paymentApprovalResultVO.isApproved() ? paymentApprovalResultVO.getAmount() : 0;
        this.buyerPhone = paymentApprovalResultVO.getBuyerPhoneNumber();
        this.buyerEmail = paymentApprovalResultVO.getBuyerEmail();
        PaymentStatus paymentStatus = paymentApprovalResultVO.isApproved() ? PaymentStatus.APPROVED : PaymentStatus.DECLINED;
        this.changePaymentStatus(paymentStatus, paymentApprovalResultVO.getResultMessage());
    }

    /**
     * 결제 망취소 결과를 반영한다.
     *
     * @param paymentNetworkCancelResultVO 결제 망취소 요청 결과 VO
     */
    public void applyPaymentNetworkCancelResult(PaymentNetworkCancelResultVO paymentNetworkCancelResultVO) {
        PaymentStatus paymentStatus = paymentNetworkCancelResultVO.isNetworkCanceled() ? PaymentStatus.NETWORK_CANCELED : PaymentStatus.NETWORK_CANCELED_FAIL;
        this.changePaymentStatus(paymentStatus, paymentNetworkCancelResultVO.getResultMessage());
    }

    /**
     * 해당 결제의 상태를 변경한다.
     *
     * @param paymentStatus 결제 상태
     * @param message       메시지
     */
    public void changePaymentStatus(PaymentStatus paymentStatus, String message) {
        this.paymentStatus = paymentStatus;
        PaymentLog paymentLog = new PaymentLog(this, paymentStatus, message);
        this.paymentLogList.add(paymentLog);
    }
}