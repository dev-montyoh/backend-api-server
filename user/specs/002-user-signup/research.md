# Phase 0 Research: 공개 회원가입

기존 코드베이스를 조사해 Technical Context의 미확정 항목을 해소한다.

## R1. 계정 생성 로직 재사용 가능 여부

- **Decision**: 기존 `UserCreateService.create(UserCreateCommand)`를 그대로 재사용한다.
- **Rationale**: 해당 서비스가 이미 (1) `existsByLoginId` 중복 검사 → `DUPLICATE_USER_INFO`, (2) `EncryptUtil.encode`(BCrypt) 암호화, (3) `userNo` 생성, (4) 저장을 수행한다. 부트스트랩 어드민(`ApplicationReadyEventListener`)이 동일 경로를 사용 중이라 검증된 로직이다.
- **Alternatives considered**: 회원가입 전용 서비스 신설 → 도메인 규칙 중복이 되어 기각(원칙 II/YAGNI).

## R2. 비밀번호 해시 방식

- **Decision**: 기존 `EncryptUtil`(Spring Security `BCryptPasswordEncoder`) 유지.
- **Rationale**: 로그인 검증(`UserVerifyPasswordService.match`)이 동일 인코더를 쓰므로 가입/검증 대칭이 보장된다.
- **Alternatives considered**: Argon2 등 → 현재 인증 경로 전체를 바꿔야 하므로 이번 범위 밖.

## R3. 입력 검증 위치

- **Decision**: 형식 검증(빈 값·최소 길이)은 요청 DTO에서 Jakarta Bean Validation(`@NotBlank`, `@Size`)으로, 유일성 같은 도메인 불변식은 도메인(`UserCreateService`)에서 유지.
- **Rationale**: 형식 오류는 도메인에 도달하기 전에 거르는 것이 계층 책임에 맞다. 유일성은 저장소 상태에 의존하므로 도메인 책임.
- **⚠️ 헌법 공백**: 이 분리 기준이 헌법에 명문화돼 있지 않아 관행으로 결정함 → 개정 후보 #2.

## R4. 공개 경로 노출(게이트웨이)

- **Decision**: 회원가입 경로를 게이트웨이 `WhiteListProperties`에 추가해 비인증 접근을 허용한다.
- **Rationale**: 원칙 III — 인증 예외는 코드가 아니라 화이트리스트 설정으로 관리.
- **Alternatives considered**: user 서비스 자체 인증 예외 처리 → 중앙 인증 원칙 위반이라 기각.

## R5. 성공 응답 형태

- **Decision**: 생성 결과를 나타내는 최소 응답(예: userNo 또는 생성 확인). 비밀번호/해시/내부 PK(userId) 비노출.
- **Rationale**: FR-003/FR-007, SC-004. loginId는 클라이언트가 이미 알고 있으므로 반환 불필요.
- **Open**: 반환 필드 확정은 data-model/contract에서 구체화.

## 미해소 항목

- 없음. (비밀번호 최소 길이는 spec Assumptions에서 8자 기본값으로 확정.)
