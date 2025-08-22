package io.github.monty.api.payment.domain.model.entity;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "payment_inicis")
public class InicisPayment extends Payment {

    public InicisPayment(InicisPaymentCreateCommand inicisPaymentCreateCommand) {
        super(inicisPaymentCreateCommand);
        this.authToken = inicisPaymentCreateCommand.getAuthToken();
        this.idcCode = inicisPaymentCreateCommand.getIdcName();
        this.approvalUrl = inicisPaymentCreateCommand.getAuthorizationUrl();
        this.cancelUrl = inicisPaymentCreateCommand.getNetCancelUrl();
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
    @Column(name = "cancel_url", nullable = false)
    private String cancelUrl;

    @Size(max = 20)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "approval_date_time")
    private LocalDateTime approvalDateTime;

    /**
     * 결제 승인 결과를 반영한다.
     *
     * @param inicisPaymentApprovalResultVO 이니시스 승인 요청 결과 VO
     */
    public void applyApprovePaymentResult(InicisPaymentApprovalResultVO inicisPaymentApprovalResultVO) {
        this.paymentMethod = inicisPaymentApprovalResultVO.getPaymentMethod();
        this.approvalDateTime = inicisPaymentApprovalResultVO.getApprovalDateTime();
        super.applyApprovePaymentResult(inicisPaymentApprovalResultVO);
    }
}