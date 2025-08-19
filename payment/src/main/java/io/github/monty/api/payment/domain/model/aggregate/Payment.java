package io.github.monty.api.payment.domain.model.aggregate;

import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
public class Payment {

    @Id
    @Size(max = 100)
    @Column(name = "payment_id", nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

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

    @Size(max = 50)
    @NotNull
    @Column(name = "payment_type", nullable = false, length = 50)
    private PaymentType paymentType;

    @Size(max = 20)
    @NotNull
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;
}