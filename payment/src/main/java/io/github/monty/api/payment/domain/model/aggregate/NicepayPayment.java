package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.domain.model.command.NicepayPaymentCreateCommand;
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
@Table(name = "payment_nicepay", schema = "payment")
public class NicepayPayment extends Payment {

    /**
     * 인증 후 결제 데이터 생성
     */
    public NicepayPayment(String paymentNo, NicepayPaymentCreateCommand nicepayPaymentCreateCommand) {
        super(paymentNo, nicepayPaymentCreateCommand);
        this.authToken = nicepayPaymentCreateCommand.getAuthToken();
        this.signature = nicepayPaymentCreateCommand.getSignature();
        this.nextApprovalUrl = nicepayPaymentCreateCommand.getNextApprovalUrl();
        this.networkCancelUrl = nicepayPaymentCreateCommand.getNetworkCancelUrl();
    }

    @NotNull
    @Column(name = "auth_token", nullable = false, length = Integer.MAX_VALUE)
    private String authToken;

    @NotNull
    @Column(name = "signature", nullable = false, length = Integer.MAX_VALUE)
    private String signature;

    @Size(max = 255)
    @NotNull
    @Column(name = "next_approval_url", nullable = false)
    private String nextApprovalUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "network_cancel_url", nullable = false)
    private String networkCancelUrl;
}