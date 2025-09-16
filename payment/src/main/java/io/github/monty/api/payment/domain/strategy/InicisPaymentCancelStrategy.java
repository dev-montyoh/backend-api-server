package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.constants.PaymentServiceProviderType;
import io.github.monty.api.payment.common.constants.PaymentStatus;
import io.github.monty.api.payment.common.exception.ApplicationException;
import io.github.monty.api.payment.common.utils.ConvertUtils;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.aggregate.InicisPayment;
import io.github.monty.api.payment.domain.model.aggregate.Payment;
import io.github.monty.api.payment.domain.model.command.PaymentCancelCommand;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCancelRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentCancelResultVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelRequestVO;
import io.github.monty.api.payment.domain.model.vo.InicisPaymentNetworkCancelResultVO;
import io.github.monty.api.payment.domain.repository.InicisRepository;
import io.github.monty.api.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InicisPaymentCancelStrategy implements PaymentCancelStrategy {

    private final PaymentRepository paymentRepository;
    private final InicisRepository inicisRepository;

    private static final String NETWORK_CANCEL_SIGNATURE_MESSAGE_FORMAT = "authToken={0}&timestamp={1}";
    private static final String NETWORK_CANCEL_VERIFICATION_MESSAGE_FORMAT = "authToken={0}&signKey={1}&timestamp={2}";

    private static final String CANCEL_HASH_DATA_FORMAT = "%s%s%s%s%s";
    private static final String CANCEL_TYPE = "refund";
    private static final String CANCEL_TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    @Value("${payment.type.inicis.sign.key}")
    private String inicisSignKey;

    @Value("${payment.type.inicis.mid}")
    private String inicisMid;

    @Value("${payment.type.inicis.iniapikey}")
    private String iniApiKey;

    @Override
    public PaymentServiceProviderType getPaymentType() {
        return PaymentServiceProviderType.INICIS;
    }

    /**
     * 해당 결제번호에 해당되는 결제를 망취소 요청한다.
     *
     * @param paymentNo 결제 번호
     */
    @Override
    public void networkCancelPayment(String paymentNo) {
        InicisPayment inicisPayment = this.getInicisPayment(paymentNo);
        try {
            long timestamp = System.currentTimeMillis();
            String plainTextSignature = MessageFormat.format(NETWORK_CANCEL_SIGNATURE_MESSAGE_FORMAT, inicisPayment.getAuthToken(), String.valueOf(timestamp));
            String plainTextVerification = MessageFormat.format(NETWORK_CANCEL_VERIFICATION_MESSAGE_FORMAT, inicisPayment.getAuthToken(), inicisSignKey, String.valueOf(timestamp));

            String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
            String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);

            InicisPaymentNetworkCancelRequestVO inicisPaymentNetworkCancelRequestVO = new InicisPaymentNetworkCancelRequestVO(inicisMid, inicisPayment.getAuthToken(), timestamp, signature, verification, inicisPayment.getNetworkCancelUrl());
            InicisPaymentNetworkCancelResultVO inicisPaymentNetworkCancelResultVO = inicisRepository.requestNetworkCancelPayment(inicisPaymentNetworkCancelRequestVO);
            inicisPayment.applyPaymentNetworkCancelResult(inicisPaymentNetworkCancelResultVO);
        } catch (Exception exception) {
            //  망취소 실패 처리
            inicisPayment.applyPaymentFail(PaymentStatus.NETWORK_CANCELED_FAIL);
            log.error(exception.getMessage(), exception);
            throw exception;
        } finally {
            paymentRepository.save(inicisPayment);
        }
    }

    /**
     * 해당 결제번호에 해당되는 결제를 취소 요청한다.
     *
     * @param paymentCancelCommand 결제 취소 요청 Command
     */
    @Override
    public void cancelPayment(PaymentCancelCommand paymentCancelCommand) {
        InicisPayment inicisPayment = this.getInicisPayment(paymentCancelCommand.getPaymentNo());
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            String timestamp = localDateTime.format(DateTimeFormatter.ofPattern(CANCEL_TIMESTAMP_PATTERN));
            InicisPaymentCancelRequestVO.Data data = new InicisPaymentCancelRequestVO.Data(inicisPayment.getTransactionId(), paymentCancelCommand.getCancelReason());
            String plainTextHashData = String.format(CANCEL_HASH_DATA_FORMAT, iniApiKey, inicisMid, CANCEL_TYPE, timestamp, ConvertUtils.convertToString(data));

            String hashData = EncryptUtils.encrypt(plainTextHashData, EncryptType.SHA512);

            InicisPaymentCancelRequestVO inicisPaymentCancelRequestVO = new InicisPaymentCancelRequestVO(inicisMid, CANCEL_TYPE, timestamp, "127.0.0.1", hashData, data);
            InicisPaymentCancelResultVO inicisPaymentCancelResultVO = inicisRepository.requestCancelPayment(inicisPaymentCancelRequestVO);
            inicisPayment.applyPaymentCancelResult(inicisPaymentCancelResultVO);
        } catch (Exception exception) {
            //  결제 취소 실패 처리
            inicisPayment.applyPaymentFail(PaymentStatus.CANCELED_FAIL);
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public void partialCancelPayment() {

    }

    @Override
    public void virtualAccountCancelPayment() {

    }

    /**
     * InicisPayment 데이터를 조회한다.
     *
     * @param paymentNo 결제 번호
     * @return 조회 결과
     */
    private InicisPayment getInicisPayment(String paymentNo) {
        Optional<Payment> paymentOptional = paymentRepository.findByPaymentNo(paymentNo);
        return (InicisPayment) paymentOptional.orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_PAYMENT_DATA));
    }

    /**
     * 현재 IP를 조회해서 반환한다.
     *
     * @return IP 조회 결과
     */
    private String getIpAddress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }
}
