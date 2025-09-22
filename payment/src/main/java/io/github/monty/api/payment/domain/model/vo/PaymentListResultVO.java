package io.github.monty.api.payment.domain.model.vo;

import lombok.Builder;

import java.util.List;

@Builder
public record PaymentListResultVO(List<Payment> paymentList, long totalPages, long totalCount) {
    @Builder
    public record Payment(String paymentNo, String orderNo, long amount, long cancelAmount, String paymentStatus, String approvalDateTime,
                          String paymentServiceProviderType, String createdAt) {
    }
}
