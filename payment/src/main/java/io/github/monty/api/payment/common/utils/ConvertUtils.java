package io.github.monty.api.payment.common.utils;

import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;

@Slf4j
@UtilityClass
public class ConvertUtils {

    /**
     * 해당 객체를 MultiValueMap 형식으로 변환한다.
     *
     * @param object 변환 대상 객체
     * @return 변환 결과
     */
    public static MultiValueMap<String, String> convertToMultiValueMap(Object object) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if (!ObjectUtils.isEmpty(field.get(object)))
                    formData.add(field.getName(), field.get(object).toString());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return formData;
    }
}
