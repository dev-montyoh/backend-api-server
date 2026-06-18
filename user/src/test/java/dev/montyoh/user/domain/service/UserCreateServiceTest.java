package dev.montyoh.user.domain.service;

import dev.montyoh.user.common.constants.ErrorCode;
import dev.montyoh.user.common.exception.ApplicationException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreateServiceTest {

    @InjectMocks
    private UserCreateService userCreateService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("신규 유저 생성에 성공한다.")
    void create_success() {
        //  given
        given(userRepository.existsByLoginId(anyString())).willReturn(false);
        UserCreateCommand command = UserCreateCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when
        userCreateService.create(command);

        //  then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("이미 존재하는 loginId로 유저 생성 시 예외가 발생한다.")
    void create_duplicate_login_id() {
        //  given
        given(userRepository.existsByLoginId(anyString())).willReturn(true);
        UserCreateCommand command = UserCreateCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();

        //  when,   then
        ApplicationException actual = assertThrows(ApplicationException.class, () -> userCreateService.create(command));
        assertAll(
                () -> assertThat(actual.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_USER_INFO.getCode()),
                () -> assertThat(actual.getMessage()).isEqualTo(ErrorCode.DUPLICATE_USER_INFO.getMessage())
        );
    }
}
