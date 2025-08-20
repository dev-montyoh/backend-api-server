package io.github.monty.api.payment.domain.model.entity;

import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}