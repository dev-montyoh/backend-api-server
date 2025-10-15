package io.github.monty.api.payment.domain.model.entity;

import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "PAYMENT_LOG", schema = "payment")
public class PaymentLog extends BaseEntity {

    public PaymentLog(Payment payment, PaymentStatus paymentStatus, String message) {
        this.payment = payment;
        this.paymentStatus = paymentStatus;
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_LOG_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAYMENT_ID", nullable = false)
    private Payment payment;

    @NotNull
    @Column(name = "PAYMENT_STATUS", nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @Column(name = "MESSAGE")
    private String message;
}