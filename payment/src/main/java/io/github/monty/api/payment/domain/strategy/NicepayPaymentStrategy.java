package io.github.monty.api.payment.domain.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.monty.api.payment.common.constants.*;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.aggregate.NicepayPayment;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.NicepayPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.NicepayPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.*;
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
public class NicepayPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final NicepayRepository nicepayRepository;

    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    //  EdiDate + MID + Amt + MerchantKey
    private static final String AUTHENTICATION_SIGN_DATA_MESSAGE_FORMAT = "{0}{1}{2}{3}";
    //  AuthToken + MID + Amt + MerchantKey
    private static final String AUTHENTICATION_RESPONSE_SIGNATURE_MESSAGE_FORMAT = "{0}{1}{2}{3}";
    //  AuthToken + MID + Amt + EdiDate + MerchantKey
    private static final String APPROVAL_SIGN_DATA_MESSAGE_FORMAT = "{0}{1}{2}{3}{4}";
    //  TID + MID + Amt + MerchantKey
    private static final String APPROVAL_RESPONSE_SIGNATURE_MESSAGE_FORMAT = "{0}{1}{2}{3}";

    @Value("${payment.type.nicepay.merchantKey}")
    private String merchantKey;

    @Value("${payment.type.nicepay.mid}")
    private String nicepayMid;

    @Override
    public PaymentServiceProviderType getPaymentType() {
        return PaymentServiceProviderType.NICEPAY;
    }

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     * 나이스페이 결제에 필요한 값들 암호화 처리
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    @Override
    public PaymentSignatureResVo getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        NicepayPaymentSignatureQuery nicepayPaymentSignatureQuery = (NicepayPaymentSignatureQuery) paymentSignatureQuery;
        String ediDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        String plainTextSignData = MessageFormat.format(AUTHENTICATION_SIGN_DATA_MESSAGE_FORMAT, ediDate, nicepayMid, nicepayPaymentSignatureQuery.getPrice(), merchantKey);
        String signData = EncryptUtils.encrypt(plainTextSignData, EncryptType.SHA256);

        return new NicepayPaymentSignatureResVo(nicepayMid, ediDate, signData);
    }

    /**
     * 해당 결제 정보를 바탕으로 결제 데이터를 저장한다.
     *
     * @param paymentCreateCommand 결제 인증 정보 저장 요청 Command
     * @return 저장 결과
     */
    @Override
    public PaymentCreateResVo createPayment(PaymentCreateCommand paymentCreateCommand) {
        NicepayPaymentCreateCommand nicepayPaymentCreateCommand = (NicepayPaymentCreateCommand) paymentCreateCommand;
        this.validateSignature(AUTHENTICATION_RESPONSE_SIGNATURE_MESSAGE_FORMAT, nicepayPaymentCreateCommand.getSignature(), nicepayPaymentCreateCommand.getAuthToken(), nicepayMid, String.valueOf(nicepayPaymentCreateCommand.getPrice()), merchantKey);
        String paymentNo = this.generatePaymentNo(nicepayPaymentCreateCommand.getPaymentServiceProviderType());
        NicepayPayment nicepayPayment = new NicepayPayment(paymentNo, nicepayPaymentCreateCommand);
        Payment payment = paymentRepository.save(nicepayPayment);
        return NicepayPaymentCreateResVo.builder()
                .paymentNo(payment.getPaymentNo())
                .build();
    }

    /**
     * 해당 결제번호에 해당되는 결제를 승인 요청한다.
     *
     * @param paymentNo 결제 번호
     */
    @Override
    public void approvePayment(String paymentNo) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(paymentNo);
        NicepayPayment nicepayPayment = (NicepayPayment) paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
        try {
            String ediDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

            String plainTextSignData = MessageFormat.format(APPROVAL_SIGN_DATA_MESSAGE_FORMAT, nicepayPayment.getAuthToken(), nicepayMid, String.valueOf(nicepayPayment.getAmount()), ediDate, merchantKey);
            String signData = EncryptUtils.encrypt(plainTextSignData, EncryptType.SHA256);

            NicepayPaymentApprovalReqVo nicepayPaymentApprovalReqVo = new NicepayPaymentApprovalReqVo(nicepayPayment, nicepayMid, ediDate, signData);
            NicepayPaymentApprovalResVo nicepayPaymentApprovalResVo = nicepayRepository.requestApprovePayment(nicepayPaymentApprovalReqVo);
            this.validateSignature(APPROVAL_RESPONSE_SIGNATURE_MESSAGE_FORMAT, nicepayPaymentApprovalResVo.getSignature(), nicepayPaymentApprovalResVo.getTid(), nicepayMid, nicepayPaymentApprovalResVo.getAmount(), merchantKey);
            nicepayPayment.applyPaymentApprovalResult(nicepayPaymentApprovalResVo);
        } catch (ApplicationException applicationException) {
            String errorMessage = StringUtils.hasText(applicationException.getErrorMessage()) ? applicationException.getErrorMessage() : StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR;
            nicepayPayment.applyPaymentFail(PaymentStatus.DECLINED, errorMessage);
            log.error(applicationException.getMessage(), applicationException);
            throw applicationException;
        }  catch (Exception exception) {
            //  승인 실패 처리
            nicepayPayment.applyPaymentFail(PaymentStatus.DECLINED, StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR);
            log.error(exception.getMessage(), exception);
            throw exception;
        } finally {
            paymentRepository.save(nicepayPayment);
        }
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
