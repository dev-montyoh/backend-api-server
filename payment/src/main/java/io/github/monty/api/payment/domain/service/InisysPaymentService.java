package io.github.monty.api.payment.domain.service;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.query.InisysPaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.command.PaymentCreateCommand;
import io.github.monty.api.payment.domain.model.query.PaymentSignatureQuery;
import io.github.monty.api.payment.domain.model.vo.InisysPaymentSignatureResultResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentCreateResultVO;
import io.github.monty.api.payment.domain.model.vo.PaymentSignatureResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class InisysPaymentService implements PaymentService {
    private static final String SIGNATURE_MESSAGE_FORMAT = "oid={0}&price={1}&timestamp={2}";
    private static final String VERIFICATION_MESSAGE_FORMAT = "oid={0}&price={1}&signKey={2}&timestamp={3}";

    @Value("${payment.type.inisys.sign.key}")
    private String inisysSignKey;

    @Value("${payment.type.inisys.mid}")
    private String inisysMid;

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.INISYS;
    }

    @Override
    public PaymentSignatureResultVO getSignature(PaymentSignatureQuery paymentSignatureQuery) {
        InisysPaymentSignatureQuery inisysPaymentSignatureQuery = (InisysPaymentSignatureQuery) paymentSignatureQuery;
        long timestamp = System.currentTimeMillis();

        String plainTextSignature = MessageFormat.format(SIGNATURE_MESSAGE_FORMAT, inisysPaymentSignatureQuery.getOid(), inisysPaymentSignatureQuery.getPrice(), String.valueOf(timestamp));
        String plainTextVerification = MessageFormat.format(VERIFICATION_MESSAGE_FORMAT, inisysPaymentSignatureQuery.getOid(), inisysPaymentSignatureQuery.getPrice(), inisysSignKey, String.valueOf(timestamp));

        String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
        String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);
        String mKey = EncryptUtils.encrypt(inisysSignKey, EncryptType.SHA256);

        return new InisysPaymentSignatureResultResultVO(signature, verification, mKey, inisysMid,  timestamp);
    }

    @Override
    public PaymentCreateResultVO createPayment(PaymentCreateCommand paymentCreateCommand) {

        return null;
    }
}
