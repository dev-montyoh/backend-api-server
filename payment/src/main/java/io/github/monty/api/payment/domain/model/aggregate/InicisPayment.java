package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
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
@Table(name = "payment_inicis", schema = "payment")
public class InicisPayment extends Payment {

    /**
     * 인증 후 결제 데이터 생성
     */
    public InicisPayment(String paymentNo, InicisPaymentCreateCommand inicisPaymentCreateCommand) {
        super(paymentNo, inicisPaymentCreateCommand);
        this.authToken = inicisPaymentCreateCommand.getAuthToken();
        this.idcCode = inicisPaymentCreateCommand.getIdcCode();
        this.approvalUrl = inicisPaymentCreateCommand.getApprovalUrl();
        this.networkCancelUrl = inicisPaymentCreateCommand.getNetworkCancelUrl();
    }

    @NotNull
    @Column(name = "AUTH_TOKEN", nullable = false)
    private String authToken;

    @Size(max = 20)
    @NotNull
    @Column(name = "IDC_CODE", nullable = false, length = 20)
    private String idcCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "APPROVAL_URL", nullable = false)
    private String approvalUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "NETWORK_CANCEL_URL", nullable = false)
    private String networkCancelUrl;
}