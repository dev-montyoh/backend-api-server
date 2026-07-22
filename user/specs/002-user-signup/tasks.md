# Tasks: 공개 회원가입 (Public User Registration)

**Feature**: `002-user-signup` | **Module**: `user`
**Input**: [plan.md](plan.md), [spec.md](spec.md), [data-model.md](data-model.md), [contracts/signup.md](contracts/signup.md)

테스트 포함(기존 모듈이 JaCoCo 80% 강제 — 신규 코드도 테스트 필요).

## Phase 1: Setup

- [x] T001 신규 의존성 불필요 확인 (validation·mapstruct 이미 `user/build.gradle`에 존재) — no-op 검증

## Phase 2: Foundational (모든 스토리 선행)

- [x] T002 `user/.../interfaces/rest/constants/UserApiUrl.java`에 `Signup.USER_SIGNUP_URL = "/users"` 추가
- [x] T003 `user/.../domain/service/UserCreateService.java`의 `create()` 반환형 void→`User`로 변경 (저장된 엔티티 반환, 부트스트랩 호출부 영향 없음)
- [x] T004 `user/.../application/commandservice/UserCreateCommandService.java`의 `create()` 반환형 void→`User`로 변경 (위임)

## Phase 3: User Story 1 — 신규 회원가입 (P1)

**Goal**: 방문자가 loginId/password로 계정 생성 → 즉시 로그인 가능
**Independent Test**: 미사용 아이디로 가입 201 → 같은 자격증명 로그인 200

- [x] T005 [P] [US1] `user/.../interfaces/rest/dto/UserSignupReqDto.java` 생성 (record, `@NotBlank`/`@Size(min=8)`)
- [x] T006 [P] [US1] `user/.../interfaces/rest/dto/UserSignupRspDto.java` 생성 (record `userNo`, 민감정보 없음)
- [x] T007 [US1] `user/.../interfaces/rest/mapper/UserSignupCommandMapper.java` 생성 (MapStruct, req→Command / User→RspDto)
- [x] T008 [US1] `user/.../interfaces/rest/controller/SignupController.java` 생성 (`POST /users`, `@Valid`, 201)
- [x] T009 [US1] gateway `config/whitelist.yaml`에 `"/api/user/v1/users"` 추가 (공개 경로 — 원칙 III)
- [x] T010 [P] [US1] `SignupControllerTest` (성공 201) / `UserCreateServiceTest`(성공) / `UserSignupCommandMapperTest`

## Phase 4: User Story 2 — 중복 아이디 방지 (P1)

**Goal**: 존재하는 아이디 가입 거부(409), 기존 계정 불변
**Independent Test**: 존재 아이디 재가입 → 409

- [x] T011 [US2] 중복 검사 재사용 확인 (`UserCreateService.existsByLoginId`→`DUPLICATE_USER_INFO` 이미 존재, 신규 코드 없음)
- [x] T012 [US2] `UserCreateServiceTest`에 중복→`DUPLICATE_USER_INFO` 케이스 추가

## Phase 5: User Story 3 — 입력 검증 (P2)

**Goal**: 빈 값/짧은 비밀번호 거부(400), 계정 미생성
**Independent Test**: 빈 아이디/짧은 pw → 400

- [x] T013 [US3] `user/.../common/constants/ErrorCode.java`에 `INVALID_INPUT("0304", ..., BAD_REQUEST)` 추가
- [x] T014 [US3] `ApplicationExceptionHandler`에 `MethodArgumentNotValidException` 핸들러 추가 — 헤더 규약(`code`/`message`)으로 통일 (헌법 VII)
- [x] T015 [US3] `SignupControllerTest`에 검증 실패(400) 케이스 추가

## Phase 6: Polish & Cross-Cutting

- [x] T016 springdoc 어노테이션(`@Tag`/`@Operation`) 확인 (SignupController에 포함)
- [x] T017 `./gradlew :user:build :gateway:build`로 빌드·테스트·커버리지 통과 확인
- [x] T018 baseline [001-user-service](spec.md) 이미 갱신됨 확인 (userNo 설명 / signup 교차참조)

## Dependencies

- Phase 2 → Phase 3~5 (선행 필수)
- US1(P1) = MVP. US2·US3는 US1 위에서 독립 검증 가능.

## MVP

US1 (T001–T010) 만으로 "가입→로그인" 핵심 흐름 완성. US2/US3는 안전성·품질 증분.
