package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.*;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.aggregate.InicisPayment;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.InicisPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.*;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InicisPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final InicisRepository inicisRepository;

    private static final String AUTHENTICATION_SIGNATURE_MESSAGE_FORMAT = "oid={0}&price={1}&timestamp={2}";
    private static final String AUTHENTICATION_VERIFICATION_MESSAGE_FORMAT = "oid={0}&price={1}&signKey={2}&timestamp={3}";

    private static final String APPROVAL_SIGNATURE_MESSAGE_FORMAT = "authToken={0}&timestamp={1}";
    private static final String APPROVAL_VERIFICATION_MESSAGE_FORMAT = "authToken={0}&signKey={1}&timestamp={2}";

    @Value("${payment.type.inicis.sign.key}")
    private String inicisSignKey;

    @Value("${payment.type.inicis.mid}")
    private String inicisMid;

    @Override
    public PaymentServiceProviderType getPaymentType() {
        return PaymentServiceProviderType.INICIS;
    }

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     * 이니시스 결제에 필요한 값들 암호화 처리
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    @Override
    public PaymentSignatureResVo getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        InicisPaymentSignatureQuery inicisPaymentSignatureQuery = (InicisPaymentSignatureQuery) paymentSignatureQuery;
        long timestamp = System.currentTimeMillis();

        String plainTextSignature = MessageFormat.format(AUTHENTICATION_SIGNATURE_MESSAGE_FORMAT, inicisPaymentSignatureQuery.getOid(), inicisPaymentSignatureQuery.getPrice(), String.valueOf(timestamp));
        String plainTextVerification = MessageFormat.format(AUTHENTICATION_VERIFICATION_MESSAGE_FORMAT, inicisPaymentSignatureQuery.getOid(), inicisPaymentSignatureQuery.getPrice(), inicisSignKey, String.valueOf(timestamp));

        String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
        String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);
        String mKey = EncryptUtils.encrypt(inicisSignKey, EncryptType.SHA256);

        return new InicisPaymentSignatureResVo(signature, verification, mKey, inicisMid, timestamp);
    }

    /**
     * 해당 결제 정보를 바탕으로 결제 데이터를 저장한다.
     * 이니시스 결제 정보를 저장하고 반환한다.
     *
     * @param paymentCreateCommand 결제 인증 정보 저장 요청 Command
     * @return 저장 결과
     */
    @Override
    public PaymentCreateResVo createPayment(PaymentCreateCommand paymentCreateCommand) {
        InicisPaymentCreateCommand inicisPaymentCreateCommand = (InicisPaymentCreateCommand) paymentCreateCommand;
        String paymentNo = this.generatePaymentNo(paymentCreateCommand.getPaymentServiceProviderType());
        InicisPayment inicisPayment = new InicisPayment(paymentNo, inicisPaymentCreateCommand);
        Payment payment = paymentRepository.save(inicisPayment);
        return InicisPaymentCreateResVo.builder()
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
        InicisPayment inicisPayment = (InicisPayment) paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
        try {
            long timestamp = System.currentTimeMillis();
            String plainTextSignature = MessageFormat.format(APPROVAL_SIGNATURE_MESSAGE_FORMAT, inicisPayment.getAuthToken(), String.valueOf(timestamp));
            String plainTextVerification = MessageFormat.format(APPROVAL_VERIFICATION_MESSAGE_FORMAT, inicisPayment.getAuthToken(), inicisSignKey, String.valueOf(timestamp));

            String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
            String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);

            InicisPaymentApprovalReqVo inicisPaymentApprovalReqVo = new InicisPaymentApprovalReqVo(inicisMid, inicisPayment.getAuthToken(), timestamp, signature, verification, inicisPayment.getApprovalUrl());
            InicisPaymentApprovalResVo inicisPaymentApprovalResVo = inicisRepository.requestApprovePayment(inicisPaymentApprovalReqVo);
            inicisPayment.applyPaymentApprovalResult(inicisPaymentApprovalResVo);
        } catch (ApplicationException applicationException) {
            String errorMessage = StringUtils.hasText(applicationException.getErrorMessage()) ? applicationException.getErrorMessage() : StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR;
            inicisPayment.applyPaymentFail(PaymentStatus.DECLINED, errorMessage);
            log.error(applicationException.getMessage(), applicationException);
            throw applicationException;
        } catch (Exception exception) {
            //  승인 실패 처리
            inicisPayment.applyPaymentFail(PaymentStatus.DECLINED, StaticValues.DEFAULT_MESSAGE_PAYMENT_APPROVAL_ERROR);
            log.error(exception.getMessage(), exception);
            throw exception;
        } finally {
            paymentRepository.save(inicisPayment);
        }
    }
}
