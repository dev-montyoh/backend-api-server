package io.github.monty.api.payment.domain.model.entity;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
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
@Table(name = "inicis_payments")
public class InicisPayment extends Payment {
    @NotNull
    @Column(name = "auth_token", nullable = false)
    private String authToken;

    @Size(max = 20)
    @NotNull
    @Column(name = "idc_name", nullable = false, length = 20)
    private String idcName;

    @Size(max = 255)
    @NotNull
    @Column(name = "auth_url", nullable = false)
    private String authUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "net_cancel_url", nullable = false)
    private String netCancelUrl;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payments;

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