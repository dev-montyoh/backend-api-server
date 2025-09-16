package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentCancelType;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.entity.BaseEntity;
import io.github.monty.api.payment.domain.model.entity.PaymentLog;
import io.github.monty.api.payment.domain.model.vo.PaymentApprovalResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentCancelResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentNetworkCancelResultVO;
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
@Table(name = "payment")
public class Payment extends BaseEntity {

    /**
     * 인증 후 결제 데이터 생성
     */
    public Payment(String paymentNo, PaymentCreateCommand paymentCreateCommand) {
        this.transactionId = null;
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
    @Column(name = "payment_id", nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Size(max = 100)
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Size(max = 100)
    @NotNull
    @Column(name = "payment_no", nullable = false, length = 100)
    private String paymentNo;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Size(max = 100)
    @NotNull
    @Column(name = "order_no", nullable = false, length = 100)
    private String orderNo;

    @NotNull
    @Column(name = "pg_provider_type", nullable = false, length = 50)
    private PaymentServiceProviderType paymentServiceProviderType;

    @NotNull
    @Column(name = "payment_status", nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @Size(max = 20)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "approval_date_time")
    private LocalDateTime approvalDateTime;

    @Size(max = 20)
    @Column(name = "buyer_phone", length = 20)
    private String buyerPhone;

    @Size(max = 50)
    @Column(name = "buyer_email", length = 50)
    private String buyerEmail;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogList;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentCancel> paymentCancelList;

    /**
     * 결제 승인 결과를 반영한다.
     *
     * @param paymentApprovalResultVO 승인 요청 결과 VO
     */
    public void applyPaymentApprovalResult(PaymentApprovalResultVO paymentApprovalResultVO) {
        this.transactionId = paymentApprovalResultVO.getTid();
        this.buyerPhone = paymentApprovalResultVO.getBuyerPhoneNumber();
        this.buyerEmail = paymentApprovalResultVO.getBuyerEmail();
        this.paymentMethod = paymentApprovalResultVO.getPaymentMethod();
        if (paymentApprovalResultVO.isApproved()) {
            this.approvalDateTime = paymentApprovalResultVO.getApprovalDateTime();
            this.paymentStatus = PaymentStatus.APPROVED;
            this.addPaymentLog(PaymentStatus.APPROVED, paymentApprovalResultVO.getResultMessage());
        } else {
            this.paymentStatus = PaymentStatus.DECLINED;
            this.addPaymentLog(PaymentStatus.DECLINED, paymentApprovalResultVO.getResultMessage());
        }
    }

    /**
     * 결제 취소 결과를 반영한다.
     *
     * @param paymentCancelResultVO 결제 취소 요청 결과 VO
     */
    public void applyPaymentCancelResult(PaymentCancelResultVO paymentCancelResultVO) {
        if (paymentCancelResultVO.isCancelled()) {
            this.addPaymentCancel(PaymentCancelType.CANCEL, this.amount, paymentCancelResultVO.getReason());
            this.paymentStatus = PaymentStatus.CANCELED;
            this.addPaymentLog(PaymentStatus.CANCELED, paymentCancelResultVO.getResultMessage());
        } else {
            this.paymentStatus = PaymentStatus.CANCELED_FAIL;
            this.addPaymentLog(PaymentStatus.CANCELED_FAIL, paymentCancelResultVO.getResultMessage());
        }
    }

    /**
     * 결제 망취소 결과를 반영한다.
     *
     * @param paymentNetworkCancelResultVO 결제 망취소 요청 결과 VO
     */
    public void applyPaymentNetworkCancelResult(PaymentNetworkCancelResultVO paymentNetworkCancelResultVO) {
        if (paymentNetworkCancelResultVO.isNetworkCanceled()) {
            this.addPaymentCancel(PaymentCancelType.NETWORK_CANCEL, this.amount, StaticValues.DEFAULT_REASON_PAYMENT_NETWORK_CANCEL);
            this.paymentStatus = PaymentStatus.NETWORK_CANCELED;
            this.addPaymentLog(PaymentStatus.NETWORK_CANCELED, paymentNetworkCancelResultVO.getResultMessage());
        } else {
            this.paymentStatus = PaymentStatus.NETWORK_CANCELED_FAIL;
            this.addPaymentLog(PaymentStatus.NETWORK_CANCELED_FAIL, paymentNetworkCancelResultVO.getResultMessage());
        }
    }

    /**
     * 해당 결제의 실패 처리를 한다.
     *
     * @param paymentStatus 결제 실패 상태
     */
    public void applyPaymentFail(PaymentStatus paymentStatus) {
        String message = StringUtils.EMPTY;
        message = switch (paymentStatus) {
            case DECLINED -> StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR;
            case CANCELED_FAIL -> StaticValues.DEFAULT_MESSAGE_PAYMENT_CANCEL_ERROR;
            case NETWORK_CANCELED_FAIL -> StaticValues.DEFAULT_MESSAGE_PAYMENT_NETWORK_CANCEL_ERROR;
            default -> message;
        };
        this.paymentStatus = paymentStatus;
        this.addPaymentLog(paymentStatus, message);
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