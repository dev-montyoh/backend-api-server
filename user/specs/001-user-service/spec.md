# Feature Specification: User 서비스 (as-built baseline)

**Service Module**: `user`

**Created**: 2026-07-22

**Status**: Baseline (역방향 추출 — 현재 구현 상태를 명세로 고정)

**Input**: 기존 코드베이스(`user/`)에서 리버스 엔지니어링한 as-built 명세.
앞으로의 user 관련 기능은 이 baseline을 기준으로 정방향 SDD로 확장한다.

## 서비스 책임 (Scope)

`user` 서비스는 **사용자 계정과 로그인**을 담당한다.

- 사용자 자격증명(loginId/password) 보관 및 비밀번호 검증
- 로그인 처리 및 토큰 발급 오케스트레이션 (토큰 자체 발급은 `auth` 서비스에 위임)
- 애플리케이션 기동 시 초기 어드민 계정 부트스트랩

**범위 밖 (Out of scope, 현재 미구현):**

- 회원가입(공개 REST 회원 생성) 엔드포인트 — 현재 유저 생성은 부트스트랩 경로로만 존재
  *(→ [002-user-signup](../002-user-signup/spec.md)에서 정방향 SDD로 추가 명세됨)*
- 프로필 조회/수정, 회원 탈퇴, 권한(Role) 관리 — 권한은 `auth` 서비스 소관

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 회원 로그인 (Priority: P1)

등록된 사용자가 loginId와 password로 로그인하면, 이후 인증에 사용할 토큰(access/refresh)을 발급받는다.

**Why this priority**: user 서비스가 외부에 노출하는 유일한 REST 기능이며, 전체 인증 플로우의 진입점이다.

**Independent Test**: 유효한 자격증명으로 `POST /api/user/v1/users/login`을 호출해 200과 토큰이 반환되는지, 잘못된 자격증명으로 인증 실패 응답이 오는지로 독립 검증 가능.

**Acceptance Scenarios**:

1. **Given** 존재하는 loginId와 올바른 password, **When** 로그인 API를 호출하면, **Then** `auth` 서비스가 발급한 토큰을 담은 200 응답을 반환한다.
2. **Given** 존재하는 loginId와 틀린 password, **When** 로그인 API를 호출하면, **Then** 비밀번호 검증 실패 에러를 반환하고 토큰을 발급하지 않는다.
3. **Given** 존재하지 않는 loginId, **When** 로그인 API를 호출하면, **Then** 인증 실패 에러를 반환한다.

---

### User Story 2 - 초기 어드민 자동 생성 (Priority: P2)

운영자가 `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD` 환경변수를 설정한 상태로 서비스를 기동하면, 시스템이 자동으로 어드민 계정을 생성해 최초 로그인이 가능한 상태를 만든다.

**Why this priority**: 공개 회원가입이 없는 현재, 시스템에 진입 가능한 최초 계정을 만드는 유일한 경로다.

**Independent Test**: 두 환경변수를 설정하고 기동 → 해당 loginId로 로그인되는지 확인. 같은 상태로 재기동해도 중복 생성되지 않는지 확인.

**Acceptance Scenarios**:

1. **Given** `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD`가 설정됨 + 해당 loginId 미존재, **When** 애플리케이션 기동이 완료되면, **Then** 어드민 계정을 1건 생성한다.
2. **Given** 두 환경변수 중 하나라도 비어 있음, **When** 기동되면, **Then** 생성을 건너뛰고 경고 로그를 남긴다(기동은 정상 진행).
3. **Given** 동일 loginId 계정이 이미 존재, **When** 재기동되면, **Then** 생성을 건너뛰고(멱등) 기동은 정상 진행한다.

### Edge Cases

- 로그인 시 `auth` 서비스가 응답 불가/에러이면? → 토큰 발급 실패로 처리 (Feign 에러 디코더 경유).
- 비밀번호는 평문 저장하지 않는다 — 저장·검증 모두 해시 기반(`EncryptUtil`).
- 초기 어드민 생성 중 예외 발생 시 애플리케이션 기동 자체는 실패시키지 않는다(로그만 남기고 계속).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 시스템은 `POST /api/user/v1/users/login` (gateway 경유 `/user/v1/users/login`)으로 loginId/password를 받아 로그인을 처리해야 한다.
- **FR-002**: 시스템은 로그인 시 저장된 해시와 대조해 비밀번호를 검증해야 하며, 실패 시 토큰을 발급하지 않아야 한다.
- **FR-003**: 비밀번호 검증 성공 시 시스템은 `auth` 서비스에 토큰 발급을 위임(Feign)하고, 그 결과 토큰을 응답으로 반환해야 한다.
- **FR-004**: 사용자 비밀번호는 해시 형태로만 저장해야 하며 평문으로 저장/반환하지 않아야 한다.
- **FR-005**: 애플리케이션 기동 완료 시 `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD`가 모두 설정된 경우에만 초기 어드민 계정을 생성해야 한다.
- **FR-006**: 초기 어드민 생성은 멱등해야 한다 — 동일 loginId가 이미 있으면 건너뛴다.
- **FR-007**: 시스템은 `GET /user/v1/monitor/healthcheck` 헬스체크 엔드포인트를 제공해야 한다.

### Key Entities

- **User (aggregate)**: 사용자 계정. 영속 테이블 `users`. 속성:
  - `userId` — 내부 PK, UUID 자동 생성, **외부 비노출**
  - `userNo` — 외부 노출용 식별자, UUID를 base36으로 축약한 20자 이내 문자열 (`UserCreateService.generateUserNo()`)
  - `loginId` — 로그인 식별자, **고유**
  - `password` — BCrypt 해시 (`EncryptUtil`), 평문 저장·반환 금지

## 인터페이스 (as-built)

| 기능 | 메서드 | 경로(외부/gateway) | 서비스 내부 경로 |
|------|--------|--------------------|------------------|
| 로그인 | POST | `/api/user/v1/users/login` | `/user/v1/users/login` |
| 헬스체크 | GET | `/api/user/v1/monitor/healthcheck` | `/user/v1/monitor/healthcheck` |

## 의존성 (Dependencies)

- **auth 서비스** (OpenFeign): 토큰 발급(`AuthFeignClient` → `auth`의 토큰 발급 API). user는 토큰을 직접 만들지 않는다.
- **DB**: `users` 테이블 (JPA). *(구체 DB 벤더는 프로필 설정에 따름 — 최근 PostgreSQL로 마이그레이션됨)*
- **gateway**: 외부 요청은 gateway의 `/api/user/**` 라우트를 통해 유입되며 인증은 gateway에서 중앙 처리된다.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 유효한 자격증명 로그인 요청이 유효한 토큰을 반환한다 (정상 경로 성공률 100%).
- **SC-002**: 잘못된 비밀번호/미존재 계정 로그인은 토큰을 발급하지 않고 명확한 인증 실패로 응답한다.
- **SC-003**: 환경변수가 설정된 클린 환경 최초 기동 시 어드민 계정 1건이 생성되어 즉시 로그인 가능하다.
- **SC-004**: 어드민이 존재하는 상태로 재기동해도 계정 수는 변하지 않는다(중복 0건).
- **SC-005**: 모듈 라인 커버리지 80% 이상 (헌법 원칙 V).

## Assumptions

- 회원가입/프로필/탈퇴는 현재 범위 밖이며, 필요 시 향후 정방향 SDD로 별도 명세한다.
- 사용자 권한(Role/UserRole)의 소유·검증은 `auth` 서비스 책임으로 가정한다.
- 초기 어드민 계정은 운영 부트스트랩 용도이며, 자격증명은 환경변수로만 주입된다(소스 하드코딩 금지).
