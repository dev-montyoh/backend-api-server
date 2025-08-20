package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.InicisPaymentApproveRequestVO;

public interface InicisRepository {
    void requestApprovePayment(InicisPaymentApproveRequestVO inicisPaymentApproveRequestVO);
}
