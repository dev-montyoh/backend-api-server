package io.github.monty.api.payment.domain.model.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Size(max = 100)
    @Column(name = "pay_no", nullable = false, length = 100)
    private String payNo;


}