package io.github.monty.api.payment.domain.strategy;

import io.github.monty.api.payment.common.constants.EncryptType;
import io.github.monty.api.payment.common.constants.PaymentType;
import io.github.monty.api.payment.common.utils.EncryptUtils;
import io.github.monty.api.payment.domain.model.query.InisysPaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.query.PaymentAuthInfoQuery;
import io.github.monty.api.payment.domain.model.vo.InisysPaymentAuthInfoVO;
import io.github.monty.api.payment.domain.model.vo.PaymentAuthInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class InisysPaymentStrategy implements PaymentStrategy {
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
    public PaymentAuthInfoVO getAuthInfo(PaymentAuthInfoQuery paymentAuthInfoQuery) {
        InisysPaymentAuthInfoQuery inisysPaymentAuthInfoQuery = (InisysPaymentAuthInfoQuery) paymentAuthInfoQuery;
        long timestamp = System.currentTimeMillis();

        String plainTextVerification = MessageFormat.format(VERIFICATION_MESSAGE_FORMAT, inisysPaymentAuthInfoQuery.getOid(), inisysPaymentAuthInfoQuery.getPrice(), inisysSignKey, timestamp);
        String plainTextSignature = MessageFormat.format(SIGNATURE_MESSAGE_FORMAT, inisysPaymentAuthInfoQuery.getOid(), inisysPaymentAuthInfoQuery.getPrice(), timestamp);

        String verification = EncryptUtils.encrypt(plainTextVerification, EncryptType.SHA256);
        String signature = EncryptUtils.encrypt(plainTextSignature, EncryptType.SHA256);
        String mKey = EncryptUtils.encrypt(inisysSignKey, EncryptType.SHA256);

        return new InisysPaymentAuthInfoVO(verification, signature, mKey, inisysMid);
    }
}
