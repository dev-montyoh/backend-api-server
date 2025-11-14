package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.*;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.aggregate.NicepayPayment;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCancelReqVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentCancelResVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentNetworkCancelReqVo;
import io.github.monty.api.payment.domain.model.vo.NicepayPaymentNetworkCancelResVo;
import io.github.monty.api.payment.domain.repository.NicepayRepository;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NicepayPaymentCancelStrategy implements PaymentCancelStrategy {

    private final PaymentRepository paymentRepository;
    private final NicepayRepository nicepayRepository;

    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    //  AuthToken + MID + Amt + EdiDate + MerchantKey
    private static final String NET_CANCEL_SIGN_DATA_MESSAGE_FORMAT = "{0}{1}{2}{3}{4}";
    //  TID + MID + CancelAmt + MerchantKey
    private static final String NET_CANCEL_RESPONSE_SIGNATURE_MESSAGE_FORMAT = "{0}{1}{2}{3}";
    //  MID + CancelAmt + EdiDate + MerchantKey
    private static final String CANCEL_SIGN_DATA_MESSAGE_FORMAT = "{0}{1}{2}{3}";
    //  TID + MID + CancelAmt + MerchantKey
    private static final String CANCEL_RESPONSE_SIGNATURE_MESSAGE_FORMAT = "{0}{1}{2}{3}";


    @Value("${payment.type.nicepay.merchantKey}")
    private String merchantKey;

    @Value("${payment.type.nicepay.mid}")
    private String nicepayMid;

    @Override
    public PaymentServiceProviderType getPaymentType() {
        return PaymentServiceProviderType.NICEPAY;
    }

    /**
     * 해당 결제번호에 해당되는 결제를 망취소 요청한다.
     *
     * @param paymentNo 결제 번호
     */
    @Override
    public void networkCancelPayment(String paymentNo) {
        NicepayPayment nicepayPayment = this.getNicepayPayment(paymentNo);
        try {
            String ediDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

            String plainTextSignData = MessageFormat.format(NET_CANCEL_SIGN_DATA_MESSAGE_FORMAT, nicepayPayment.getAuthToken(), nicepayMid, String.valueOf(nicepayPayment.getAmount()), ediDate, merchantKey);
            String signData = EncryptUtils.encrypt(plainTextSignData, EncryptType.SHA256);

            NicepayPaymentNetworkCancelReqVo nicepayPaymentNetworkCancelReqVo = new NicepayPaymentNetworkCancelReqVo(nicepayPayment, nicepayMid, ediDate, signData);
            NicepayPaymentNetworkCancelResVo nicepayPaymentNetworkCancelResVo = nicepayRepository.requestNetworkCancelPayment(nicepayPaymentNetworkCancelReqVo);
            this.validateSignature(NET_CANCEL_RESPONSE_SIGNATURE_MESSAGE_FORMAT, nicepayPaymentNetworkCancelResVo.getSignature(), nicepayPaymentNetworkCancelResVo.getTid(), nicepayMid, nicepayPaymentNetworkCancelResVo.getCancelAmount(), merchantKey);
            nicepayPayment.applyPaymentNetworkCancelResult(nicepayPaymentNetworkCancelResVo);
        } catch (ApplicationException applicationException) {
            String errorMessage = StringUtils.hasText(applicationException.getErrorMessage()) ? applicationException.getErrorMessage() : StaticValues.DEFAULT_MESSAGE_PAYMENT_NETWORK_CANCEL_ERROR;
            nicepayPayment.applyPaymentFail(PaymentStatus.NETWORK_CANCELED_FAIL, errorMessage);
            log.error(applicationException.getMessage(), applicationException);
            throw applicationException;
        } catch (Exception exception) {
            //  망취소 실패 처리
            nicepayPayment.applyPaymentFail(PaymentStatus.NETWORK_CANCELED_FAIL, StaticValues.DEFAULT_MESSAGE_PAYMENT_NETWORK_CANCEL_ERROR);
            log.error(exception.getMessage(), exception);
            throw exception;
        } finally {
            paymentRepository.save(nicepayPayment);
        }
    }

    /**
     * 해당 결제번호에 해당되는 결제를 취소 요청한다.
     *
     * @param paymentCancelCommand 결제 취소 요청 Command
     */
    @Override
    public void cancelPayment(PaymentCancelCommand paymentCancelCommand) {
        NicepayPayment nicepayPayment = this.getNicepayPayment(paymentCancelCommand.getPaymentNo());
        try {
            String ediDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

            String plainTextSignData = MessageFormat.format(CANCEL_SIGN_DATA_MESSAGE_FORMAT, nicepayMid, String.valueOf(nicepayPayment.getAmount()), ediDate, merchantKey);
            String signData = EncryptUtils.encrypt(plainTextSignData, EncryptType.SHA256);

            NicepayPaymentCancelReqVo nicepayPaymentCancelReqVo = new NicepayPaymentCancelReqVo(nicepayPayment, nicepayMid, ediDate, signData, paymentCancelCommand.getCancelReason());
            NicepayPaymentCancelResVo nicepayPaymentCancelResVo = nicepayRepository.requestCancelPayment(nicepayPaymentCancelReqVo);
            this.validateSignature(
                    CANCEL_RESPONSE_SIGNATURE_MESSAGE_FORMAT,
                    nicepayPaymentCancelResVo.getSignature(),
                    nicepayPaymentCancelResVo.getCancelTransactionId(),
                    nicepayMid,
                    nicepayPaymentCancelResVo.getCancelAmount(),
                    merchantKey
            );
            nicepayPayment.applyPaymentCancelResult(nicepayPaymentCancelResVo);
        } catch (ApplicationException applicationException) {
            String errorMessage = StringUtils.hasText(applicationException.getErrorMessage()) ? applicationException.getErrorMessage() : StaticValues.DEFAULT_MESSAGE_PAYMENT_CANCEL_ERROR;
            nicepayPayment.applyPaymentFail(PaymentStatus.CANCELED_FAIL, errorMessage);
            log.error(applicationException.getMessage(), applicationException);
            throw applicationException;
        } catch (Exception exception) {
            //  망취소 실패 처리
            nicepayPayment.applyPaymentFail(PaymentStatus.CANCELED_FAIL, StaticValues.DEFAULT_MESSAGE_PAYMENT_CANCEL_ERROR);
            log.error(exception.getMessage(), exception);
            throw exception;
        } finally {
            paymentRepository.save(nicepayPayment);
        }
    }

    @Override
    public void partialCancelPayment() {

    }

    @Override
    public void virtualAccountCancelPayment() {

    }

    /**
     * NicepayPayment 데이터를 조회한다.
     *
     * @param paymentNo 결제 번호
     * @return 조회 결과
     */
    private NicepayPayment getNicepayPayment(String paymentNo) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(paymentNo);
        return (NicepayPayment) paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
    }

    /**
     * 결제 관련 요청 결과의 signature 에 대한 위변조 검증을 수행한다.
     *
     * @param messageFormat 위변조 검증 대상 메시지 포맷
     * @param signature     위변조 검증 데이터
     * @param inputs        메시지 포맷 입력 데이터
     */
    private void validateSignature(String messageFormat, String signature, String... inputs) {
        String plainTextSignature = MessageFormat.format(messageFormat, (Object[]) inputs);

        if (!signature.equals(EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256))) {
            throw new ApplicationException(ErrorCode.INVALID_ENCRYPTED_DATA);
        }
    }
}
