package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;

public interface InicisRepository {
    InicisPaymentApprovalResultVO requestApprovePayment(InicisPaymentApprovalRequestVO inicisPaymentApprovalRequestVO);
}
