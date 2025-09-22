package io.github.monty.api.payment.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.monty.api.payment.common.constants.ErrorCode;
import io.github.monty.api.payment.common.exception.ApplicationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
@UtilityClass
public class ConvertUtils {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 해당 객체를 MultiValueMap 형식으로 변환한다.
     *
     * @param object 변환 대상 객체
     * @return 변환 결과
     */
    public static MultiValueMap<String, String> convertToMultiValueMap(Object object) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if (!ObjectUtils.isEmpty(field.get(object)))
                    multiValueMap.add(field.getName(), field.get(object).toString());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return multiValueMap;
    }

    /**
     * 해당 객체를 HashMap 형식으로 변환한다.
     *
     * @param object 변환 대상 객체
     * @return 변환 결과
     */
    public static HashMap<String, String> convertToHashMap(Object object) {
        HashMap<String, String> hashMap = new LinkedHashMap<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                if (!ObjectUtils.isEmpty(field.get(object)))
                    hashMap.put(field.getName(), field.get(object).toString());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return hashMap;
    }

    /**
     * 해당 객체를 JSON 으로 변환한다.
     *
     * @param object 변환 대상 객체
     * @return 변환 결과
     */
    public static String convertToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("JSON 직렬화 실패: {}", object, jsonProcessingException);
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
}
