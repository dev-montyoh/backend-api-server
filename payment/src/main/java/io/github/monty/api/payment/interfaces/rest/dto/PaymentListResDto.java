package io.github.monty.api.payment.interfaces.rest.dto;

import java.util.List;

public record PaymentListResDto(List<Payment> paymentList, long totalPages, long totalCount) {
    public record Payment(String paymentNo, String orderNo, long amount, long cancelAmount, String paymentStatus,
                          String approvalDateTime,
                          String paymentServiceProviderType, String createdAt) {
    }
}
