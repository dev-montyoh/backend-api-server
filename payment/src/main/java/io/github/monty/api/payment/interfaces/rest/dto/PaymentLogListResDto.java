package io.github.monty.api.payment.interfaces.rest.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentLogListResDto(List<PaymentLog> paymentLogList, long totalPages, long totalCount) {
    @Builder
    public record PaymentLog(String createdAt, String paymentStatus, String message) {
    }
}
