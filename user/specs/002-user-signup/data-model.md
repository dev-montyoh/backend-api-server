# Phase 1 Data Model: 공개 회원가입

## 재사용 엔티티: User (aggregate)

신규 엔티티 없음. baseline 001의 `User` 애그리게잇을 그대로 사용한다.

| 필드 | 타입/제약 | 생성 방식 | 회원가입 시 |
|------|-----------|-----------|-------------|
| `userId` | String, PK, `@GeneratedValue(UUID)` | 저장 시 자동 | 시스템 생성 (응답 비노출) |
| `userNo` | String, ≤20자, not null | `UserCreateService.generateUserNo()` — UUID→base36 후 20자 절단 | 시스템 생성 (응답 노출 가능한 공개 식별자) |
| `loginId` | String, not null, **고유** | 요청 입력 | 검증·중복확인 대상 |
| `password` | String(255), not null | `EncryptUtil.encode`(BCrypt) | 해시 저장, 절대 반환/로그 금지 |

> **baseline 보정 필요**: 001-user-service 스펙은 `userId`/`userNo`의 구분을 설명하지 않았다.
> 정리하면 — `userId`=내부 PK(UUID, 비노출), `userNo`=외부 노출용 축약 식별자(base36 ≤20자), `loginId`=로그인 식별자.

## 검증 규칙 (요청 경계)

| 필드 | 규칙 | 위반 시 |
|------|------|---------|
| `loginId` | 비어 있지 않음 (`@NotBlank`) | 검증 실패 응답 (US3) |
| `password` | 비어 있지 않음 + 최소 8자 (`@NotBlank`, `@Size(min=8)`) | 검증 실패 응답 (US3) |

## 도메인 불변식 (저장소 경계)

- `loginId` 유일성: `userRepository.existsByLoginId(loginId)` → 존재 시 `DUPLICATE_USER_INFO` (FR-002).

## 상태 전이

없음(계정은 생성 즉시 활성; 별도 활성화 단계 없음 — FR-005).
