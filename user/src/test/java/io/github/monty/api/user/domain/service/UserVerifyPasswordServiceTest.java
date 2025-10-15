package io.github.monty.api.user.domain.service;

import io.github.monty.api.user.common.constants.ErrorCode;
import io.github.monty.api.user.common.exception.ApplicationException;
import io.github.monty.api.user.domain.model.aggregate.Member;
import io.github.monty.api.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserVerifyPasswordServiceTest {

    @InjectMocks
    private UserVerifyPasswordService userVerifyPasswordService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("userId와 password를 비교해 일치하는지 확인한다.")
    void verifyPassword_success() {
        //  given
        Member member = Member.builder()
                .loginId("testLoginId")
                .password("$2a$10$ZZby9MXeQ7LJLtnOTSbM4O44e7ze5a3O1R9Sv00to4PwDgFtlQVBG")
                .build();
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(member));

        //  when,   then
        assertDoesNotThrow(() -> userVerifyPasswordService.verifyPassword("testLoginId", "testPassword"));
    }

    @Test
    @DisplayName("회원 정보가 존재하지 않는다.")
    void verifyPassword_fail_not_found_userInfo() {
        //  when
        ApplicationException actual = assertThrows(ApplicationException.class, () -> userVerifyPasswordService.verifyPassword("testLoginId", "testPassword!"));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_USER_INFO.getMessage())
        );
    }

    @Test
    @DisplayName("패스워드가 일치하지 않아 오류가 발생했다.")
    void verifyPassword_fail_incorrect_password() {
        //  given
        Member member = Member.builder()
                .loginId("testLoginId")
                .password("$2a$10$ZZby9MXeQ7LJLtnOTSbM4O44e7ze5a3O1R9Sv00to4PwDgFtlQVBG")
                .build();
        given(userRepository.findByLoginId(anyString())).willReturn(Optional.of(member));

        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> userVerifyPasswordService.verifyPassword("testLoginId", "testPassword!"));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.INVALID_PASSWORD.getMessage())
        );
    }
}