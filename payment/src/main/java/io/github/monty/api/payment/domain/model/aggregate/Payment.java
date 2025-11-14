package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentCancelType;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.entity.BaseEntity;
import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import io.github.monty.api.payment.domain.model.vo.PaymentApprovalResVo;
import io.github.monty.api.payment.domain.model.vo.PaymentCancelResVo;
import io.github.monty.api.payment.domain.model.vo.PaymentNetworkCancelResVo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payment", schema = "payment")
public class Payment extends BaseEntity {

    /**
     * 인증 후 결제 데이터 생성
     */
    public Payment(String paymentNo, PaymentCreateCommand paymentCreateCommand) {
        this.transactionId = StringUtils.isEmpty(paymentCreateCommand.getTransactionId()) ? null : paymentCreateCommand.getTransactionId();
        this.paymentNo = paymentNo;
        this.amount = paymentCreateCommand.getPrice();
        this.orderNo = paymentCreateCommand.getOrderNo();
        this.paymentServiceProviderType = paymentCreateCommand.getPaymentServiceProviderType();
        this.approvalDateTime = null;
        this.buyerPhone = null;
        this.buyerEmail = null;
        this.paymentLogList = new ArrayList<>();
        this.paymentCancelList = new ArrayList<>();

        //  인증 상태
        this.paymentStatus = PaymentStatus.AUTHENTICATED;
        this.addPaymentLog(PaymentStatus.AUTHENTICATED, StaticValues.DEFAULT_MESSAGE_AUTHENTICATED);
    }

    @Id
    @Size(max = 100)
    @Column(name = "PAYMENT_ID", nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Size(max = 100)
    @Column(name = "TRANSACTION_ID", length = 100)
    private String transactionId;

    @Size(max = 100)
    @NotNull
    @Column(name = "PAYMENT_NO", nullable = false, length = 100)
    private String paymentNo;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    @Size(max = 100)
    @NotNull
    @Column(name = "ORDER_NO", nullable = false, length = 100)
    private String orderNo;

    @NotNull
    @Column(name = "PG_PROVIDER_TYPE", nullable = false, length = 50)
    private PaymentServiceProviderType paymentServiceProviderType;

    @NotNull
    @Column(name = "PAYMENT_STATUS", nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @Size(max = 20)
    @Column(name = "PAYMENT_METHOD", length = 20)
    private String paymentMethod;

    @Column(name = "APPROVAL_DATE_TIME")
    private LocalDateTime approvalDateTime;

    @Size(max = 20)
    @Column(name = "BUYER_PHONE", length = 20)
    private String buyerPhone;

    @Size(max = 50)
    @Column(name = "BUYER_EMAIL", length = 50)
    private String buyerEmail;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogList;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentCancel> paymentCancelList;

    /**
     * 결제 승인 결과를 반영한다.
     *
     * @param paymentApprovalResVo 승인 요청 결과 VO
     */
    public void applyPaymentApprovalResult(PaymentApprovalResVo paymentApprovalResVo) {
        this.transactionId = paymentApprovalResVo.getTid();
        this.buyerPhone = paymentApprovalResVo.getBuyerPhoneNumber();
        this.buyerEmail = paymentApprovalResVo.getBuyerEmail();
        this.paymentMethod = paymentApprovalResVo.getPaymentMethod();
        this.approvalDateTime = paymentApprovalResVo.getApprovalDateTime();
        this.paymentStatus = PaymentStatus.APPROVED;
        this.addPaymentLog(PaymentStatus.APPROVED, paymentApprovalResVo.getResultMessage());
    }

    /**
     * 결제 취소 결과를 반영한다.
     *
     * @param paymentCancelResVo 결제 취소 요청 결과 VO
     */
    public void applyPaymentCancelResult(PaymentCancelResVo paymentCancelResVo) {
        this.addPaymentCancel(PaymentCancelType.CANCEL, this.amount, paymentCancelResVo.getReason());
        this.paymentStatus = PaymentStatus.CANCELED;
        this.addPaymentLog(PaymentStatus.CANCELED, paymentCancelResVo.getResultMessage());
    }

    /**
     * 결제 망취소 결과를 반영한다.
     *
     * @param paymentNetworkCancelResVo 결제 망취소 요청 결과 VO
     */
    public void applyPaymentNetworkCancelResult(PaymentNetworkCancelResVo paymentNetworkCancelResVo) {
        this.addPaymentCancel(PaymentCancelType.NETWORK_CANCEL, this.amount, StaticValues.DEFAULT_REASON_PAYMENT_NETWORK_CANCEL);
        this.paymentStatus = PaymentStatus.NETWORK_CANCELED;
        this.addPaymentLog(PaymentStatus.NETWORK_CANCELED, paymentNetworkCancelResVo.getResultMessage());
    }

    /**
     * 해당 결제의 실패 처리를 한다.
     *
     * @param paymentStatus 결제 실패 상태
     */
    public void applyPaymentFail(PaymentStatus paymentStatus, String errorMessage) {
        this.paymentStatus = paymentStatus;
        this.addPaymentLog(paymentStatus, errorMessage);
    }

    /**
     * 해당 결제의 변경 이력을 추가한다.
     *
     * @param paymentStatus 결제 상태
     * @param message       메시지
     */
    private void addPaymentLog(PaymentStatus paymentStatus, String message) {
        PaymentLog paymentLog = new PaymentLog(this, paymentStatus, message);
        this.paymentLogList.add(paymentLog);
    }

    /**
     * 해당 결제의 취소 내역을 추가한다.
     *
     * @param paymentCancelType 결제 취소 타입
     * @param amount            취소 금액
     * @param reason            취소 사유
     */
    private void addPaymentCancel(PaymentCancelType paymentCancelType, long amount, String reason) {
        PaymentCancel paymentCancel = new PaymentCancel(this, amount, reason, paymentCancelType);
        this.paymentCancelList.add(paymentCancel);
    }
}