package io.github.monty.api.payment.interfaces.rest.dto;

import java.util.List;

public record PaymentListResponse(List<Payment> paymentList, long totalPages, long totalCount) {
    public record Payment(String orderNo, long amount, String paymentStatus, String approvalDateTime,
                          String paymentServiceProviderType) {
    }
}
