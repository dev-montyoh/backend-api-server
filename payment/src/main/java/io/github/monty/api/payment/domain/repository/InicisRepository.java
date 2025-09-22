package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.*;

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
     * 이니시스로 결제 취소 요청을 한다.
     * Content-type: application/json
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentCancelRequestVO 결제 취소 요청 VO
     * @return 취소 요청 결과 VO
     */
    InicisPaymentCancelResultVO requestCancelPayment(InicisPaymentCancelRequestVO inicisPaymentCancelRequestVO);

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
