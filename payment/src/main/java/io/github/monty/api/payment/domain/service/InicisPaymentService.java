package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.InicisPaymentApproveCommand;
import io.github.monty.api.payment.domain.model.command.InicisPaymentCreateCommand;
import io.github.monty.api.payment.domain.model.command.PaymentApproveCommand;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.entity.InicisPayment;
import io.github.monty.api.payment.domain.model.query.InicisPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.*;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InicisPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InicisRepository inicisRepository;

    private static final String AUTHENTICATION_SIGNATURE_MESSAGE_FORMAT = "oid={0}&price={1}&timestamp={2}";
    private static final String AUTHENTICATION_VERIFICATION_MESSAGE_FORMAT = "oid={0}&price={1}&signKey={2}&timestamp={3}";

    private static final String APPROVE_SIGNATURE_MESSAGE_FORMAT = "authToken={0}&timestamp={1}";
    private static final String APPROVE_VERIFICATION_MESSAGE_FORMAT = "authToken={0}&signKey={1}&timestamp={2}";

    @Value("${payment.type.inicis.sign.key}")
    private String inicisSignKey;

    @Value("${payment.type.inicis.mid}")
    private String inicisMid;

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.INICIS;
    }

    /**
     * 해당 결제의 결제 인증 정보를 반환한다.
     * 이니시스 결제에 필요한 값들 암호화 처리
     *
     * @param paymentSignatureQuery 결제 인증 정보 조회 요청 쿼리
     * @return 결제 인증 정보 생성 결과
     */
    @Override
    public PaymentSignatureResultVO getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        InicisPaymentSignatureQuery inicisPaymentSignatureQuery = (InicisPaymentSignatureQuery) paymentSignatureQuery;
        long timestamp = System.currentTimeMillis();

        String plainTextSignature = MessageFormat.format(AUTHENTICATION_SIGNATURE_MESSAGE_FORMAT, inicisPaymentSignatureQuery.getOid(), inicisPaymentSignatureQuery.getPrice(), String.valueOf(timestamp));
        String plainTextVerification = MessageFormat.format(AUTHENTICATION_VERIFICATION_MESSAGE_FORMAT, inicisPaymentSignatureQuery.getOid(), inicisPaymentSignatureQuery.getPrice(), inicisSignKey, String.valueOf(timestamp));

        String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
        String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);
        String mKey = EncryptUtils.encrypt(inicisSignKey, EncryptType.SHA256);

        return new InicisPaymentSignatureResultVO(signature, verification, mKey, inicisMid, timestamp);
    }

    /**
     * 해당 결제 정보를 바탕으로 결제 데이터를 저장한다.
     * 이니시스 결제 정보를 저장하고 반환한다.
     *
     * @param paymentCreateCommand 결제 인증 정보 저장 요청 Command
     * @return 저장 결과
     */
    @Override
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {
        InicisPaymentCreateCommand inicisPaymentCreateCommand = (InicisPaymentCreateCommand) paymentCreateCommand;
        InicisPayment inicisPayment = this.newInicisPayment(inicisPaymentCreateCommand);
        Payment payment = paymentRepository.save(inicisPayment);
        return InicisPaymentCreateResultVO.builder()
                .paymentNo(payment.getPaymentNo())
                .build();
    }

    /**
     * 해당 결제 정보를 바탕으로 결제 승인을 요청한다.
     *
     * @param paymentApproveCommand 결제 승인 요청 Command
     */
    @Override
    public void approvePayment(PaymentApproveCommand paymentApproveCommand) {
        InicisPaymentApproveCommand inicisPaymentApproveCommand = (InicisPaymentApproveCommand) paymentApproveCommand;
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(inicisPaymentApproveCommand.getPaymentNo());
        InicisPayment inicisPayment = (InicisPayment) paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));

        long timestamp = System.currentTimeMillis();
        String plainTextSignature = MessageFormat.format(APPROVE_SIGNATURE_MESSAGE_FORMAT, inicisPayment.getAuthToken(), String.valueOf(timestamp));
        String plainTextVerification = MessageFormat.format(APPROVE_VERIFICATION_MESSAGE_FORMAT, inicisPayment.getAuthToken(), inicisSignKey,  String.valueOf(timestamp));

        String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
        String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);

        InicisPaymentApproveRequestVO inicisPaymentApproveRequestVO = new InicisPaymentApproveRequestVO(inicisMid, inicisPayment.getAuthToken(), timestamp, signature, verification, inicisPayment.getAuthUrl());
        inicisRepository.requestApprovePayment(inicisPaymentApproveRequestVO);
    }

    /**
     * 이니시스 결제 엔티티 생성
     *
     * @param inicisPaymentCreateCommand 결제 생성 요청 Command
     * @return 생성된 이니시스 엔티티
     */
    private InicisPayment newInicisPayment(InicisPaymentCreateCommand inicisPaymentCreateCommand) {
        return InicisPayment.builder()
                .paymentNo(this.generatePaymentNo(inicisPaymentCreateCommand.getPaymentType()))
                .amount(0L)
                .orderNo(inicisPaymentCreateCommand.getOrderNo())
                .paymentType(inicisPaymentCreateCommand.getPaymentType())
                .paymentStatus(PaymentStatus.AUTHENTICATED)
                .authToken(inicisPaymentCreateCommand.getAuthorizationToken())
                .idcName(inicisPaymentCreateCommand.getIdcName())
                .authUrl(inicisPaymentCreateCommand.getAuthorizationUrl())
                .netCancelUrl(inicisPaymentCreateCommand.getNetCancelUrl())
                .build();
    }
}
