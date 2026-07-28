# Feature Specification: 헬스체크 (as-built baseline)

**Service Module**: `user`

**Feature Branch**: `004-health-check`

**Created**: 2026-07-28

**Status**: Baseline (역방향 추출 — 현재 구현 상태를 명세로 고정)

**Input**: baseline([001-user-service](../001-user-service/spec.md))에 FR 한 줄로만 존재하던 헬스체크를
API 단위 명세 체계로 분리한 as-built 명세. 구현 변경 없음.

## 서비스 책임 (Scope)

`GET /api/user/v1/monitor/healthcheck` — 서비스 생존 여부를 확인하는 비즈니스 로직 없는 시스템
엔드포인트. 오케스트레이션(liveness/readiness probe 등)이나 모니터링 도구가 호출한다.

**범위 밖:**

- 의존 리소스(DB, `auth` 연결 등) 상태를 포함한 상세 헬스 체크 — 현재는 단순 응답만 반환하며 하위
  의존성 상태를 검사하지 않는다.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 서비스 생존 확인 (Priority: P3)

운영자(또는 오케스트레이터)가 서비스가 살아있는지 확인하기 위해 헬스체크 엔드포인트를 호출한다.

**Why this priority**: 비즈니스 가치를 직접 만들지 않는 운영 지원 기능이나, 배포·모니터링 파이프라인의
전제 조건이다.

**Independent Test**: 인증 없이 `GET /api/user/v1/monitor/healthcheck`를 호출해 200과 고정 문자열 응답을
받는지로 독립 검증 가능.

**Acceptance Scenarios**:

1. **Given** 서비스가 정상 기동됨, **When** 헬스체크 엔드포인트를 호출하면, **Then** 200과 함께
   `"health checked"` 문자열을 반환한다.

### Edge Cases

- 애플리케이션 컨텍스트가 정상 기동되지 않은 경우 → 엔드포인트 자체가 응답하지 않음(WAS/컨테이너
  레벨에서 실패로 감지). 별도의 애플리케이션 레벨 예외 처리 없음.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 시스템은 `GET /api/user/v1/monitor/healthcheck` 엔드포인트를 제공해야 한다.
- **FR-002**: 헬스체크는 인증 없이 호출 가능해야 한다(공개 경로).
- **FR-003**: 정상 상태에서는 200과 고정 텍스트 본문(`"health checked"`)을 반환해야 한다.

### Key Entities

- 해당 없음(상태 비저장, 도메인 엔티티 관여 없음).

## 인터페이스 (as-built)

| 기능 | 메서드 | 경로(외부/gateway) | 서비스 내부 경로 |
|------|--------|--------------------|------------------|
| 헬스체크 | GET | `/api/user/v1/monitor/healthcheck` | `/user/v1/monitor/healthcheck` |

**Response 200**: `Content-Type: text/plain` (또는 기본), 본문 `"health checked"`.

## 의존성 (Dependencies)

- 없음 — DB·다른 서비스 호출 없이 고정 응답만 반환한다.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 정상 기동 상태에서 헬스체크 호출 성공률 100%(200 응답).

## Assumptions

- 현재는 "애플리케이션이 요청을 처리할 수 있는가"만 확인하며, DB 연결 등 의존 리소스 상태는 검사하지
  않는다. 더 정교한 readiness 체크가 필요해지면 별도 명세로 확장한다.
