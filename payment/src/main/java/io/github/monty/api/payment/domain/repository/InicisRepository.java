package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentApprovalResultVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResultVO;

public interface InicisRepository {

    /**
     * 이니시스로 결제 승인 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentApprovalRequestVO 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    InicisPaymentApprovalResultVO requestApprovePayment(InicisPaymentApprovalRequestVO inicisPaymentApprovalRequestVO);

    /**
     * 이니시스로 결제 망취소 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentNetworkCancelRequestVO 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    InicisPaymentNetworkCancelResultVO requestNetworkCancelPayment(InicisPaymentNetworkCancelRequestVO inicisPaymentNetworkCancelRequestVO);
}
