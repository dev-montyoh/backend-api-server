package io.github.monty.api.payment.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "payments")
public class Payment {

    @Id
    @Size(max = 100)
    @Column(name = "payment_id", nullable = false, length = 100)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

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
    private String paymentType;

    @Size(max = 100)
    @NotNull
    @Column(name = "payment_no", nullable = false, length = 100)
    private String paymentNo;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;
}