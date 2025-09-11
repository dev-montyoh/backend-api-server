package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.StaticValues;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResultVO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "payment_inicis")
public class InicisPayment extends Payment {

    public InicisPayment(String paymentNo, InicisPaymentCreateCommand inicisPaymentCreateCommand) {
        super(paymentNo, inicisPaymentCreateCommand);
        this.authToken = inicisPaymentCreateCommand.getAuthToken();
        this.idcCode = inicisPaymentCreateCommand.getIdcCode();
        this.approvalUrl = inicisPaymentCreateCommand.getApprovalUrl();
        this.networkCancelUrl = inicisPaymentCreateCommand.getNetworkCancelUrl();
    }

    @NotNull
    @Column(name = "auth_token", nullable = false)
    private String authToken;

    @Size(max = 20)
    @NotNull
    @Column(name = "idc_code", nullable = false, length = 20)
    private String idcCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "approval_url", nullable = false)
    private String approvalUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "network_cancel_url", nullable = false)
    private String networkCancelUrl;

    @Size(max = 20)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    /**
     * 결제 승인 결과를 반영한다.
     *
     * @param inicisPaymentApprovalResultVO 이니시스 승인 요청 결과 VO
     */
    public void applyPaymentApprovalResult(InicisPaymentApprovalResultVO inicisPaymentApprovalResultVO) {
        this.paymentMethod = inicisPaymentApprovalResultVO.getPaymentMethod();
        super.applyPaymentApprovalResult(inicisPaymentApprovalResultVO);
    }

    /**
     * 해당 결제를 승인 실패 처리한다.
     */
    public void applyPaymentApprovalFail() {
        super.changePaymentStatus(PaymentStatus.DECLINED, StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR);
    }

    /**
     * 결제 망취소 결과를 반영한다.
     *
     * @param inicisPaymentNetworkCancelResultVO 이니시스 결제 망취소 요청 결과 VO
     */
    public void applyPaymentNetworkCancelResult(InicisPaymentNetworkCancelResultVO inicisPaymentNetworkCancelResultVO) {
        super.applyPaymentNetworkCancelResult(inicisPaymentNetworkCancelResultVO);
    }

    /**
     * 해당 결제를 망취소 실패 처리한다.
     */
    public void applyPaymentNetworkCancelFail() {
        super.changePaymentStatus(PaymentStatus.NETWORK_CANCELED_FAIL, StaticValues.DEFAULT_MESSAGE_PAYMENT_NETWORK_CANCEL_ERROR);
    }
}