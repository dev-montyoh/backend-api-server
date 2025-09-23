package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.*;

public interface InicisRepository {

    /**
     * 이니시스로 결제 승인 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentApprovalReqVo 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    InicisPaymentApprovalResVo requestApprovePayment(InicisPaymentApprovalReqVo inicisPaymentApprovalReqVo);

    /**
     * 이니시스로 결제 취소 요청을 한다.
     * Content-type: application/json
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentCancelReqVo 결제 취소 요청 VO
     * @return 취소 요청 결과 VO
     */
    InicisPaymentCancelResVo requestCancelPayment(InicisPaymentCancelReqVo inicisPaymentCancelReqVo);

    /**
     * 이니시스로 결제 망취소 요청을 한다.
     * Content-type: application/x-www-form-urlencoded
     * HTTP Method : POST
     * 통신방식 : http-Client 통신
     *
     * @param inicisPaymentNetworkCancelReqVo 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    InicisPaymentNetworkCancelResVo requestNetworkCancelPayment(InicisPaymentNetworkCancelReqVo inicisPaymentNetworkCancelReqVo);
}
