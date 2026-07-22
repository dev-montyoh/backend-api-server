package dev.montyoh.user.interfaces.rest.controller;

import dev.montyoh.user.application.commandservice.UserCreateCommandService;
import dev.montyoh.user.domain.model.aggregate.User;
import dev.montyoh.user.domain.model.command.UserCreateCommand;
import dev.montyoh.user.interfaces.rest.constants.UserApiUrl;
import dev.montyoh.user.interfaces.rest.dto.UserSignupReqDto;
import dev.montyoh.user.interfaces.rest.dto.UserSignupRspDto;
import dev.montyoh.user.interfaces.rest.mapper.UserSignupCommandMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserApiUrl.USER_V1_BASE_URL)
@Tag(name = "User Signup API", description = "회원가입 관련 API")
public class SignupController {

    private final UserCreateCommandService userCreateCommandService;

    private final UserSignupCommandMapper userSignupCommandMapper;

    /**
     * 회원가입
     * 신규 회원을 생성하고 생성된 사용자 번호를 반환한다.
     *
     * @param userSignupReqDto 회원가입 요청 Dto
     * @return 생성된 사용자 번호를 담은 응답
     */
    @Operation(summary = "회원 가입 API", description = "신규 회원을 생성한 후 사용자 번호를 반환한다.")
    @PostMapping(value = UserApiUrl.Signup.USER_SIGNUP_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSignupRspDto> signup(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        UserCreateCommand userCreateCommand = userSignupCommandMapper.mapToCommand(userSignupReqDto);
        User user = userCreateCommandService.create(userCreateCommand);
        UserSignupRspDto userSignupRspDto = userSignupCommandMapper.mapToRspDto(user);
        return new ResponseEntity<>(userSignupRspDto, HttpStatus.CREATED);
    }
}
