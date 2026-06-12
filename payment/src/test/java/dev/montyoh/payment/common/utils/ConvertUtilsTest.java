package dev.montyoh.payment.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ConvertUtilsTest {

    @Getter
    @AllArgsConstructor
    static class TestDto {
        private String name;
        private String value;
    }

    @Test
    @DisplayName("객체를 MultiValueMap으로 변환한다.")
    void convertToMultiValueMap_success() {
        //  given
        TestDto dto = new TestDto("testName", "testValue");

        //  when
        MultiValueMap<String, String> actual = ConvertUtils.convertToMultiValueMap(dto);

        //  then
        assertAll(
                () -> assertThat(actual.getFirst("name")).isEqualTo("testName"),
                () -> assertThat(actual.getFirst("value")).isEqualTo("testValue")
        );
    }

    @Test
    @DisplayName("null 필드를 포함하는 객체를 MultiValueMap으로 변환 시 null 필드는 제외된다.")
    void convertToMultiValueMap_skipNullFields() {
        //  given
        TestDto dto = new TestDto("testName", null);

        //  when
        MultiValueMap<String, String> actual = ConvertUtils.convertToMultiValueMap(dto);

        //  then
        assertAll(
                () -> assertThat(actual.getFirst("name")).isEqualTo("testName"),
                () -> assertThat(actual.containsKey("value")).isFalse()
        );
    }

    @Test
    @DisplayName("객체를 HashMap으로 변환한다.")
    void convertToHashMap_success() {
        //  given
        TestDto dto = new TestDto("testName", "testValue");

        //  when
        HashMap<String, String> actual = ConvertUtils.convertToHashMap(dto);

        //  then
        assertAll(
                () -> assertThat(actual.get("name")).isEqualTo("testName"),
                () -> assertThat(actual.get("value")).isEqualTo("testValue")
        );
    }

    @Test
    @DisplayName("객체를 JSON 문자열로 변환한다.")
    void convertToString_success() {
        //  given
        TestDto dto = new TestDto("testName", "testValue");

        //  when
        String actual = ConvertUtils.convertToString(dto);

        //  then
        assertThat(actual).contains("testName").contains("testValue");
    }
}
