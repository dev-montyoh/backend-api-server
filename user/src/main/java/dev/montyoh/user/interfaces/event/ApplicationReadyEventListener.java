package dev.montyoh.user.interfaces.event;

import dev.montyoh.user.application.commandservice.UserCreateCommandService;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationReadyEventListener {

    private final UserCreateCommandService userCreateCommandService;

    /**
     * 애플리케이션 구동 완료 시 초기 어드민 유저를 생성한다.
     * ADMIN_LOGIN_ID, ADMIN_PASSWORD 환경변수가 설정된 경우에만 실행되며,
     * 이미 존재하는 loginId라면 생성을 건너뜬다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String loginId = System.getenv("ADMIN_LOGIN_ID");
        String password = System.getenv("ADMIN_PASSWORD");

        if (!StringUtils.hasText(loginId) || !StringUtils.hasText(password)) {
            log.warn("ADMIN_LOGIN_ID 또는 ADMIN_PASSWORD 환경변수가 설정되지 않아 초기 유저 생성을 건너뜁니다.");
            return;
        }

        try {
            UserCreateCommand userCreateCommand = UserCreateCommand.builder()
                    .loginId(loginId)
                    .password(password)
                    .build();
            userCreateCommandService.create(userCreateCommand);
            log.info("초기 어드민 유저가 생성되었습니다. loginId: {}", loginId);
        } catch (Exception e) {
            log.info("초기 어드민 유저 생성 건너뜀: {}", e.getMessage());
        }
    }
}
