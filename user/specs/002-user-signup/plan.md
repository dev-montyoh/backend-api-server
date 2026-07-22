# Implementation Plan: 공개 회원가입 (Public User Registration)

**Branch**: `002-user-signup` | **Date**: 2026-07-22 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `user/specs/002-user-signup/spec.md`

## Summary

방문자가 로그인 아이디/비밀번호로 계정을 만드는 **공개 회원가입 REST 엔드포인트**를 추가한다.
계정 생성 도메인 로직(`UserCreateService` — 중복 검사 + BCrypt 암호화 + 저장)은 이미 존재하며 부트스트랩 어드민 경로가 사용 중이다. 따라서 이 기능은 **도메인을 재사용**하고 `interfaces/rest` 계층(컨트롤러·요청/응답 DTO·검증·매퍼)과 게이트웨이 화이트리스트 등록만 추가하는 얇은 작업이다.

## Technical Context

**Language/Version**: Java 19

**Primary Dependencies**: Spring Boot (Web MVC), Spring Data JPA, MapStruct, Lombok, springdoc(OpenAPI), Spring Security Crypto(BCrypt, 기존 `EncryptUtil`), Jakarta Bean Validation

**Storage**: `user` 모듈 전용 DB, `users` 테이블 (JPA). *(벤더는 프로필 설정 — 최근 PostgreSQL 마이그레이션)*

**Testing**: JUnit + JaCoCo (라인 커버리지 80% 게이트)

**Target Platform**: Linux 컨테이너 (Docker), 게이트웨이 뒤 내부 서비스

**Project Type**: MSA 백엔드 서비스 (단일 모듈 `user`)

**Performance Goals**: 일반 웹 회원가입 수준 — 특별한 고부하 목표 없음

**Constraints**: 공개 엔드포인트(비인증) — 게이트웨이 화이트리스트 필요. 비밀번호는 응답·로그 비노출.

**Scale/Scope**: 개인 프로젝트 규모. 엔드포인트 1개, 신규 도메인 규칙 없음.

## Constitution Check

*GATE: Phase 0 전에 통과해야 함. Phase 1 설계 후 재점검.*

| 원칙 | 판정 | 근거 / 조치 |
|------|:----:|------------|
| I. 서비스 경계 분리 | ✅ PASS | user 모듈 내부에서 완결. 다른 서비스 DB 접근·신규 서비스 호출 없음(로그인과 달리 auth 호출 불필요). |
| II. DDD 계층형 | ✅ PASS | 도메인(`UserCreateService`)·애플리케이션(`UserCreateCommandService`) 재사용. 추가는 `interfaces/rest`(컨트롤러/DTO/매퍼)뿐. 계층 방향 유지. |
| III. 게이트웨이 중앙 인증 | ⚠️ PASS (조치 필요) | 회원가입은 공개 → **화이트리스트에 경로 등록 필요**(하드코딩 아님, `WhiteListProperties`). 이 조치를 tasks에 명시. |
| IV. 설정·시크릿 외부화 | ✅ PASS | 비밀번호 최소 길이 정책은 프로필/설정값으로 외부화 권장(코드 상수 하드코딩 지양). |
| V. 테스트 게이트 | ✅ PASS (조치 필요) | 컨트롤러 성공/중복/검증실패 경로 테스트로 80% 유지. |
| VI. 문서·코드 일치 | ⚠️ 조치 필요 | springdoc 어노테이션 추가. **baseline 001 스펙의 "signup 범위 밖" 서술을 갱신**해야 함(이 기능이 그 범위를 채움). |

### ⚠️ 헌법이 답을 주지 못한 지점 (이번 plan에서 발견된 GAP)

이 게이트를 채우면서 **헌법에 없어서 임의로 결정해야 했던** 항목들 — 별도로 헌법 개정 후보로 보고한다:

1. **표준 에러 응답 계약 부재**: 중복(`DUPLICATE_USER_INFO`)·검증 실패 시 응답 형태를 헌법이 규정하지 않음. 각 서비스에 `ErrorCode` + `ApplicationExceptionHandler`라는 사실상의 표준이 있으나 원칙으로 명문화돼 있지 않음.
2. **입력 검증 위치 규약 부재**: 형식 검증(빈 값/최소 길이)을 DTO(`@Valid`)에서 할지 도메인에서 할지 헌법이 침묵. (현 코드는 중복 검사를 도메인에서 함 → 이 plan은 "형식=DTO, 불변식=도메인"으로 결정했으나 근거가 헌법이 아니라 관행.)
3. **응답·로그의 자격증명 위생(PII)**: "비밀번호를 응답·로그에 남기지 않는다"는 횡단 보안 규칙인데 헌법(원칙 IV는 시크릿 외부화만 다룸)에 없음. 기능마다 재선언 중.
4. **공개 엔드포인트 거버넌스**: 신규 공개(화이트리스트) 경로 추가 시 "명시적 정당화 + 보안 검토"를 요구하는 게이트가 원칙 III에 없음.

## Project Structure

### Documentation (this feature)

```text
user/specs/002-user-signup/
├── plan.md              # 이 파일
├── research.md          # Phase 0 산출물
├── data-model.md        # Phase 1 산출물
├── quickstart.md        # Phase 1 산출물
├── contracts/           # Phase 1 산출물
└── tasks.md             # /speckit-tasks 산출물 (plan에서 생성하지 않음)
```

### Source Code (user 모듈)

기존 계층 구조를 그대로 따른다. **★** 표시가 이번에 신규/변경.

```text
user/src/main/java/dev/montyoh/user/
├── interfaces/rest/
│   ├── controller/
│   │   ├── LoginController.java
│   │   └── SignupController.java            # ★ 신규 (POST /users)
│   ├── dto/
│   │   ├── UserSignupReqDto.java            # ★ 신규 (@NotBlank, @Size)
│   │   └── UserSignupRspDto.java            # ★ 신규 (민감정보 없음)
│   ├── mapper/
│   │   └── UserSignupCommandMapper.java     # ★ 신규 (MapStruct)
│   └── constants/
│       └── UserApiUrl.java                  # ★ 변경 (Signup.USER_SIGNUP_URL 추가)
├── application/commandservice/
│   └── UserCreateCommandService.java        # 재사용 (변경 없음)
├── domain/
│   ├── service/UserCreateService.java       # 재사용 (변경 없음)
│   ├── model/aggregate/User.java            # 재사용
│   └── repository/UserRepository.java       # 재사용 (existsByLoginId 이미 존재)
└── common/exception/                        # 재사용 (ApplicationException/ErrorCode)

gateway/src/main/java/dev/montyoh/gateway/common/property/
└── WhiteListProperties.java (+ 설정)        # ★ 변경 (회원가입 경로 공개 허용)

user/src/test/java/...                       # ★ 신규 (컨트롤러/검증 테스트)
```

**Structure Decision**: 신규 모듈·계층을 만들지 않고 `user` 모듈의 기존 DDD 계층에 `interfaces/rest` 요소만 추가한다(원칙 II). 게이트웨이 화이트리스트 변경 1건이 유일한 모듈 외 변경(원칙 III).

## Complexity Tracking

> Constitution Check에 정당화가 필요한 위반 없음 — 비워 둠.

이 기능은 기존 도메인 로직 재사용으로 신규 복잡도를 추가하지 않는다(YAGNI 준수).
