# Feature Specification: User 서비스 (as-built baseline)

**Service Module**: `user`

**Created**: 2026-07-22

**Status**: Baseline (역방향 추출 — 현재 구현 상태를 명세로 고정)

**Input**: 기존 코드베이스(`user/`)에서 리버스 엔지니어링한 as-built 명세.
앞으로의 user 관련 기능은 이 baseline을 기준으로 정방향 SDD로 확장한다.

**Note (2026-07-28)**: 원래 이 문서에 번들되어 있던 개별 API(로그인/헬스체크)는 API 단위 명세로
분리되었다 — [003-user-login](../003-user-login/spec.md), [004-health-check](../004-health-check/spec.md).
이 문서는 이제 User 애그리게잇 소유권과 (API가 아닌) 어드민 부트스트랩만 다룬다. 구현 변경 없음.

## 서비스 책임 (Scope)

`user` 서비스는 **사용자 계정**을 담당한다.

- User 애그리게잇(자격증명 loginId/password 해시) 소유·보관 — 실제 사용 API는
  [003-user-login](../003-user-login/spec.md), [002-user-signup](../002-user-signup/spec.md) 참조.
- 애플리케이션 기동 시 초기 어드민 계정 부트스트랩

**범위 밖:**

- 로그인 API 자체의 요청/응답·에러 처리 — [003-user-login](../003-user-login/spec.md).
- 회원가입(공개 REST 회원 생성) — [002-user-signup](../002-user-signup/spec.md).
- 헬스체크 — [004-health-check](../004-health-check/spec.md).
- 프로필 조회/수정, 회원 탈퇴, 권한(Role) 관리 — 권한은 `auth` 서비스 소관

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 초기 어드민 자동 생성 (Priority: P2)

운영자가 `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD` 환경변수를 설정한 상태로 서비스를 기동하면, 시스템이 자동으로 어드민 계정을 생성해 최초 로그인이 가능한 상태를 만든다.

**Why this priority**: 공개 회원가입([002-user-signup](../002-user-signup/spec.md)) 도입 이전에는 시스템에
진입 가능한 유일한 계정 생성 경로였고, 지금도 운영자 계정을 만드는 유일한 경로다.

**Independent Test**: 두 환경변수를 설정하고 기동 → 해당 loginId로 로그인되는지 확인. 같은 상태로 재기동해도 중복 생성되지 않는지 확인.

**Acceptance Scenarios**:

1. **Given** `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD`가 설정됨 + 해당 loginId 미존재, **When** 애플리케이션 기동이 완료되면, **Then** 어드민 계정을 1건 생성한다.
2. **Given** 두 환경변수 중 하나라도 비어 있음, **When** 기동되면, **Then** 생성을 건너뛰고 경고 로그를 남긴다(기동은 정상 진행).
3. **Given** 동일 loginId 계정이 이미 존재, **When** 재기동되면, **Then** 생성을 건너뛰고(멱등) 기동은 정상 진행한다.

### Edge Cases

- 비밀번호는 평문 저장하지 않는다 — 저장·검증 모두 해시 기반(`EncryptUtil`).
- 초기 어드민 생성 중 예외 발생 시 애플리케이션 기동 자체는 실패시키지 않는다(로그만 남기고 계속).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 사용자 비밀번호는 해시 형태로만 저장해야 하며 평문으로 저장/반환하지 않아야 한다.
- **FR-002**: 애플리케이션 기동 완료 시 `ADMIN_LOGIN_ID`/`ADMIN_PASSWORD`가 모두 설정된 경우에만 초기 어드민 계정을 생성해야 한다.
- **FR-003**: 초기 어드민 생성은 멱등해야 한다 — 동일 loginId가 이미 있으면 건너뛴다.

### Key Entities

- **User (aggregate)**: 사용자 계정. 영속 테이블 `users`. 속성:
  - `userId` — 내부 PK, UUID 자동 생성, **외부 비노출**
  - `userNo` — 외부 노출용 식별자, UUID를 base36으로 축약한 20자 이내 문자열 (`UserCreateService.generateUserNo()`)
  - `loginId` — 로그인 식별자, **고유**
  - `password` — BCrypt 해시 (`EncryptUtil`), 평문 저장·반환 금지

## 인터페이스 (as-built)

이 문서는 API를 소유하지 않는다. User 애그리게잇을 사용하는 API는
[003-user-login](../003-user-login/spec.md)(로그인), [002-user-signup](../002-user-signup/spec.md)(회원가입)을
참조.

## 의존성 (Dependencies)

- **DB**: `users` 테이블 (JPA). *(구체 DB 벤더는 프로필 설정에 따름 — 최근 PostgreSQL로 마이그레이션됨)*

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 환경변수가 설정된 클린 환경 최초 기동 시 어드민 계정 1건이 생성되어 즉시 로그인 가능하다.
- **SC-002**: 어드민이 존재하는 상태로 재기동해도 계정 수는 변하지 않는다(중복 0건).
- **SC-003**: 모듈 라인 커버리지 80% 이상 (헌법 원칙 V).

## Assumptions

- 프로필/탈퇴는 현재 범위 밖이며, 필요 시 향후 정방향 SDD로 별도 명세한다.
- 사용자 권한(Role/UserRole)의 소유·검증은 `auth` 서비스 책임으로 가정한다.
- 초기 어드민 계정은 운영 부트스트랩 용도이며, 자격증명은 환경변수로만 주입된다(소스 하드코딩 금지).
