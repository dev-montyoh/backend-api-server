# API Contract: 회원가입

## POST /api/user/v1/users  (게이트웨이 경유)

- 게이트웨이 외부 경로: `/api/user/v1/users`
- user 서비스 내부 경로: `/user/v1/users` (`UserApiUrl.USER_V1_BASE_URL` + `Signup.USER_SIGNUP_URL`)
- 인증: **불필요 (공개)** — 게이트웨이 화이트리스트 등록 필요
- Content-Type: `application/json`

> 참고: 기존 로그인은 `POST /users/login`. 회원가입은 리소스 컬렉션 `POST /users`(생성)로 두어 REST 의미에 맞춘다.

### Request Body

```json
{
  "loginId": "string, 필수, 비어있지 않음",
  "password": "string, 필수, 최소 8자"
}
```

### Responses

| 상태 | 조건 | 응답 형식 (as-built 확인됨) |
|------|------|------|
| `201 Created` | 가입 성공 | 본문에 `userNo` (비밀번호/userId 비노출) |
| `400 Bad Request` | 입력 검증 실패 (`@Valid`) | ⚠️ **현재 `ApplicationExceptionHandler` 미경유** → Spring 기본 검증 에러 JSON 본문 |
| `409 Conflict` | loginId 중복 (`DUPLICATE_USER_INFO`) | **헤더** `code: 0303`, `message`(URL-encoded), **본문 null** |

> ✅ **확인**: `DUPLICATE_USER_INFO` → `HttpStatus.CONFLICT`(409). `ErrorCode` enum에 HTTP 상태가 매핑돼 있음.
>
> ✅ **구현됨**: 헌법 v1.1 원칙 VII에 따라 `MethodArgumentNotValidException` 핸들러를 추가해 검증 실패(400)도 동일 헤더 규약(`code`=`0304 INVALID_INPUT`/`message`)으로 통일함.
>
> ⚠️ **as-built 에러 규약(중요)**: `ApplicationExceptionHandler`는 에러 코드/메시지를 **응답 헤더**(`code`/`message`, `StaticValues.HEADER_ERROR_*`)에 담고 **본문은 null**로 반환한다(JSON 에러 본문 아님). 헌법 v1.1 원칙 VII로 명문화됨.
>
> ⚠️ **형식 불일치(신규 발견)**: Jakarta `@Valid` 실패는 `ApplicationExceptionHandler`를 타지 않아 Spring 기본 400 **본문**으로 나간다. 즉 도메인 에러(헤더)와 검증 에러(본문) 형식이 갈린다. tasks에서 처리 방향 결정 필요:
> - (a) `MethodArgumentNotValidException` 핸들러를 추가해 헤더 규약으로 통일, 또는
> - (b) 헤더 규약 자체를 재검토(개정 후보 #1과 함께).

### Acceptance 매핑

- US1 → 201 + 이후 `POST /users/login` 성공
- US2 → 409, 기존 계정 불변
- US3 → 400, 계정 미생성
