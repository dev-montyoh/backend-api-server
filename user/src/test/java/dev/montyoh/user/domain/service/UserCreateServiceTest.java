package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserCreateServiceTest {

    @InjectMocks
    private UserCreateService userCreateService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("신규 유저를 생성하고 저장된 User를 반환한다.")
    void create_success() {
        //  given
        UserCreateCommand command = UserCreateCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        given(userRepository.existsByLoginId(anyString())).willReturn(false);
        given(userRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        //  when
        User created = userCreateService.create(command);

        //  then
        assertAll(
                () -> assertThat(created.getLoginId()).isEqualTo("testLoginId"),
                () -> assertThat(created.getUserNo()).isNotBlank(),
                () -> assertThat(created.getPassword()).isNotEqualTo("testPassword")
        );
    }

    @Test
    @DisplayName("이미 존재하는 loginId면 DUPLICATE_USER_INFO 오류가 발생한다.")
    void create_fail_duplicate() {
        //  given
        UserCreateCommand command = UserCreateCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        given(userRepository.existsByLoginId(anyString())).willReturn(true);

        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> userCreateService.create(command));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_INFO.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.DUPLICATE_USER_INFO.getMessage())
        );
    }
}
