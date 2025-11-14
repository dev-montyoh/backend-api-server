package io.github.monty.api.payment.domain.repository;

import io.github.monty.api.payment.domain.model.vo.*;

public interface NicepayRepository {

    /**
     * 나이스페이로 결제 승인 요청을 한다.
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentApprovalReqVo 결제 승인 요청 VO
     * @return 승인 요청 결과 VO
     */
    NicepayPaymentApprovalResVo requestApprovePayment(NicepayPaymentApprovalReqVo nicepayPaymentApprovalReqVo);

    /**
     * 나이스페이로 결제 망취소 요청을 한다.
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentNetworkCancelReqVo 결제 망취소 요청 VO
     * @return 망취소 요청 결과 VO
     */
    NicepayPaymentNetworkCancelResVo requestNetworkCancelPayment(NicepayPaymentNetworkCancelReqVo nicepayPaymentNetworkCancelReqVo);

    /**
     * 나이스페이로 결제 취소 요청을 한다. (전체)
     * Target <a href="https://pg-api.nicepay.co.kr/webapi/cancel_process.jsp">Nicepay Cancel</a>
     * Method POST
     * Content-Type application/x-www-form-urlencoded
     * Encoding euc-kr
     *
     * @param nicepayPaymentCancelReqVo 결제 취소 요청 VO
     * @return 취소 요청 결과 VO
     */
    NicepayPaymentCancelResVo requestCancelPayment(NicepayPaymentCancelReqVo nicepayPaymentCancelReqVo);
}
