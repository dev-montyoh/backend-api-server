package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentCancelType;
import io.github.monty.api.payment.domain.model.entity.BaseEntity;
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
@Table(name = "PAYMENT_CANCEL", schema = "payment")
public class PaymentCancel extends BaseEntity {

    public PaymentCancel(Payment payment, long cancelAmount, String reason, PaymentCancelType paymentCancelType) {
        this.payment = payment;
        this.cancelTransactionId = null;
        this.cancelAmount = cancelAmount;
        this.reason = reason;
        this.paymentCancelType = paymentCancelType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_CANCEL_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;

    @Size(max = 100)
    @Column(name = "CANCEL_TRANSACTION_ID", length = 100)
    private String cancelTransactionId;

    @NotNull
    @Column(name = "PAYMENT_CANCEL_TYPE", nullable = false, length = 50)
    private PaymentCancelType paymentCancelType;

    @NotNull
    @Column(name = "CANCEL_AMOUNT", nullable = false)
    private Long cancelAmount;

    @Column(name = "REASON")
    private String reason;
}
