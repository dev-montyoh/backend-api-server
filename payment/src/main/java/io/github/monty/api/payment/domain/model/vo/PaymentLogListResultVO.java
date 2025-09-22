package io.github.monty.api.payment.domain.model.vo;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentLogListResultVO (List<PaymentLog> paymentLogList, long totalPages, long totalCount){
    @Builder
    public record PaymentLog(String createdAt, String paymentStatus, String message) {}
}
