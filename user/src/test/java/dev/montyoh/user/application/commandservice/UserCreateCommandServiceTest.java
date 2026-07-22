package dev.montyoh.user.application.commandservice;

import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.domain.service.UserCreateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserCreateCommandServiceTest {

    @InjectMocks
    private UserCreateCommandService userCreateCommandService;

    @Mock
    private UserCreateService userCreateService;

    @Test
    @DisplayName("유저 생성을 도메인 서비스에 위임하고 결과를 반환한다.")
    void create_delegates() {
        //  given
        UserCreateCommand command = UserCreateCommand.builder()
                .loginId("testLoginId")
                .password("testPassword")
                .build();
        User user = User.builder()
                .userNo("testUserNo")
                .loginId("testLoginId")
                .build();
        given(userCreateService.create(any())).willReturn(user);

        //  when
        User created = userCreateCommandService.create(command);

        //  then
        assertThat(created.getUserNo()).isEqualTo("testUserNo");
    }
}
