# Feature Specification: 회원 로그인 (as-built baseline)

**Service Module**: `user`

**Feature Branch**: `003-user-login`

**Created**: 2026-07-28

**Status**: Baseline (역방향 추출 — 현재 구현 상태를 명세로 고정)

**Input**: baseline([001-user-service](../001-user-service/spec.md))에 번들되어 있던 로그인 API를
API 단위 명세 체계로 분리한 as-built 명세. 구현 변경 없음(기존 [002-user-signup](../002-user-signup/spec.md)과
같은 레벨의 개별 API 명세로 재정리).

## 서비스 책임 (Scope)

`POST /api/user/v1/users/login` — 등록된 사용자가 loginId/password로 인증하고, `auth` 서비스가 발급한
토큰(access/refresh)을 받는다. 비밀번호 검증은 `user`가 수행하고, 토큰 발급 자체는 `auth`에 위임한다.

**범위 밖:**

- 토큰 재발급(refresh), 로그아웃, 토큰 검증 — `auth` 서비스 소관.
- 계정 생성 — [002-user-signup](../002-user-signup/spec.md) 소관.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 회원 로그인 (Priority: P1)

등록된 사용자가 loginId와 password로 로그인하면, 이후 인증에 사용할 토큰(access/refresh)을 발급받는다.

**Why this priority**: user 서비스가 외부에 노출하는 핵심 REST 기능이며, 전체 인증 플로우의 진입점이다.

**Independent Test**: 유효한 자격증명으로 `POST /api/user/v1/users/login`을 호출해 200과 토큰이 반환되는지,
잘못된 자격증명으로 인증 실패 응답이 오는지로 독립 검증 가능.

**Acceptance Scenarios**:

1. **Given** 존재하는 loginId와 올바른 password, **When** 로그인 API를 호출하면, **Then** `auth` 서비스가
   발급한 accessToken/refreshToken을 담은 200 응답을 반환한다.
2. **Given** 존재하는 loginId와 틀린 password, **When** 로그인 API를 호출하면, **Then** `INVALID_PASSWORD`
   (401) 오류를 반환하고 토큰을 발급하지 않는다.
3. **Given** 존재하지 않는 loginId, **When** 로그인 API를 호출하면, **Then** `NOT_FOUND_USER_INFO`(404)
   오류를 반환한다.

### Edge Cases

- 비밀번호 검증 성공 후 토큰 발급 단계에서 `auth` 서비스가 응답 불가/에러이면? → Feign 에러 디코더 경유로
  실패 처리, 토큰은 발급되지 않는다.
- 비밀번호는 평문 저장하지 않는다 — 저장·검증 모두 해시 기반(`EncryptUtil.match`).
- 인증 실패(비밀번호 불일치/계정 없음)와 검증 실패(입력값 오류)는 서로 다른 에러 코드로 구분된다
  (`INVALID_PASSWORD`/`NOT_FOUND_USER_INFO` vs `INVALID_INPUT`).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 시스템은 `POST /api/user/v1/users/login` (gateway 경유, 서비스 내부 경로
  `/user/v1/users/login`)으로 loginId/password를 받아 로그인을 처리해야 한다.
- **FR-002**: 시스템은 loginId로 사용자를 조회해야 하며, 존재하지 않으면 `NOT_FOUND_USER_INFO`(404)를
  반환해야 한다.
- **FR-003**: 시스템은 저장된 해시와 대조해 비밀번호를 검증해야 하며, 불일치 시 `INVALID_PASSWORD`(401)를
  반환하고 토큰을 발급하지 않아야 한다.
- **FR-004**: 비밀번호 검증 성공 시 시스템은 `auth` 서비스에 토큰 발급을 위임(OpenFeign)하고, 그 결과
  accessToken/refreshToken을 응답으로 반환해야 한다.
- **FR-005**: 로그인 API는 인증 없이 호출 가능해야 한다(공개 경로, gateway whitelist 등록됨:
  `/api/user/v1/users/login`).
- **FR-006**: 에러 응답은 원칙 VII의 헤더 기반 규약(`X-Error-Code`/`X-Error-Message`, 본문 null)을 따라야
  한다.

### Key Entities

- **User (aggregate)**: [001-user-service](../001-user-service/spec.md)의 User를 그대로 참조한다
  (loginId, password 해시). 신규 엔티티 없음.

## 인터페이스 (as-built)

| 기능 | 메서드 | 경로(외부/gateway) | 서비스 내부 경로 |
|------|--------|--------------------|------------------|
| 로그인 | POST | `/api/user/v1/users/login` | `/user/v1/users/login` |

**Request** (`UserLoginReqDto`): `loginId`(String), `password`(String)

**Response 200** (`UserLoginRspDto`): `accessToken`(String), `refreshToken`(String)

**Error Responses** (헤더 기반, 본문 null):

| 상황 | HTTP | X-Error-Code | 발생 위치 |
|------|------|--------------|-----------|
| loginId 미존재 | 404 | `0301` (`NOT_FOUND_USER_INFO`) | `UserVerifyPasswordService` |
| 비밀번호 불일치 | 401 | `0302` (`INVALID_PASSWORD`) | `UserVerifyPasswordService` |

## 의존성 (Dependencies)

- **auth 서비스** (OpenFeign, `AuthFeignClient`): 비밀번호 검증 성공 후 토큰 발급 위임.
  `AuthCreateTokenService.getTokens()` → `AuthRepository.createAccessTokenAndRefreshToken(userNo)`.
- **DB**: `users` 테이블(PostgreSQL, JPA) — loginId로 사용자 조회.
- **gateway**: `/api/user/v1/users/login`은 공개(인증 불필요) whitelist 경로.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 유효한 자격증명 로그인 요청이 유효한 토큰을 반환한다(정상 경로 성공률 100%).
- **SC-002**: 잘못된 비밀번호/미존재 계정 로그인은 토큰을 발급하지 않고 각각 구분된 에러 코드로 응답한다.
- **SC-003**: 모듈 라인 커버리지 80% 이상(헌법 원칙 V).

## Assumptions

- 토큰 자체의 서명·만료 정책은 `auth` 서비스 책임이며 이 명세 범위 밖이다.
- 로그인 실패 원인(계정 없음 vs 비밀번호 불일치)을 구분해 반환하는 현재 동작을 그대로 유지한다(보안상
  사용자 열거 공격 완화가 필요해지면 별도 검토 대상 — 현재는 as-built 그대로 고정).
