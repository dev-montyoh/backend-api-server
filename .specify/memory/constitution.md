# backend-api-server Constitution

개인 프로젝트용 MSA 백엔드 API 서버의 개발 원칙과 제약을 정의한다.
이 문서는 기존 코드베이스에서 역으로 정립(reverse-engineered)한 "as-built" 헌법이며,
앞으로의 모든 기능은 이 원칙을 기준으로 명세(spec) → 계획(plan) → 구현(implement) 순으로 진행한다.

## Core Principles

### I. 서비스 경계 분리 (Service Boundary Isolation)

- 각 도메인은 독립 Gradle 모듈(`gateway`, `auth`, `user`, `content`, `payment`)로 분리한다.
- 한 서비스는 자신의 도메인 데이터만 소유·변경한다. 다른 서비스의 DB에 직접 접근하지 않는다.
- 서비스 간 통신은 명시적 계약으로만 한다:
  - 내부 서비스 호출: **OpenFeign** (예: `user` → `auth` 토큰 발급)
  - 외부 시스템 호출: **WebClient** (예: `payment` → Inicis/Nicepay PG)
- 새 개인 프로젝트는 별도 레포가 아니라 이 레포에 **서비스 모듈을 추가**하는 방식으로 확장한다.

### II. DDD 계층형 아키텍처 (Layered DDD Architecture) — NON-NEGOTIABLE

모든 서비스는 아래 4계층 패키지 구조를 따른다. 계층 간 의존은 안쪽(도메인) 방향으로만 흐른다.

- `interfaces/` — REST 컨트롤러, DTO, MapStruct 매퍼, API URL 상수
- `application/` — 유스케이스 조율 (`commandservice` / `queryservice`로 CQRS 분리)
- `domain/` — `model/aggregate`, `model/entity`, `model/vo`, `model/command`, `model/query`, 도메인 `service`, `repository`(인터페이스)
- `infrastructure/` — `repository` 구현체, JPA/QueryDSL, Feign/WebClient 어댑터

규칙:
- Repository는 `domain`에 인터페이스, `infrastructure`에 구현체를 둔다 (포트-어댑터).
- 계층·서비스 경계를 넘는 객체 변환은 손으로 매핑하지 않고 **MapStruct** 매퍼로 한다.
- 도메인 모델에 프레임워크(웹/영속성) 관심사를 섞지 않는다.

### III. 게이트웨이 중앙 인증 (Centralized Gateway Auth) — NON-NEGOTIABLE

- 모든 외부 요청은 `gateway`를 통해서만 라우팅한다. 라우트는 `/api/{service}/**` 규칙을 따르고 서비스로 전달 시 `/api/{service}` prefix를 제거한다.
- **인증·인가는 gateway에서 중앙 처리**한다. 개별 서비스는 인증된 요청을 신뢰하되, 인가가 필요한 지점은 헤더로 전달된 사용자 컨텍스트를 사용한다.
- 인증 예외 경로는 코드가 아니라 **WhiteList 설정**으로 관리한다 (하드코딩 금지).
- MCP 계열 경로(`/api/mcp/**`)는 별도 인증 매니저와 **별도 서명키**로 검증한다. 일반 사용자 JWT와 MCP JWT의 키·검증 경로를 섞지 않는다.
- 토큰 서명키·시크릿은 코드에 하드코딩하지 않고 프로필/환경변수로 주입한다.

### IV. 설정과 시크릿의 외부화 (Externalized Config & Secrets)

- 환경별 설정은 Spring 프로필로 분리한다: `application-local.*`, `application-prod.*`, (필요 시 `application-develop.*`).
- `application-local.*` 등 로컬 전용/민감 설정 파일은 `.gitignore`로 커밋에서 제외한다.
- DB 접속 정보, 토큰 서명키, PG 자격증명, 어드민 초기 계정(`ADMIN_LOGIN_ID`/`ADMIN_PASSWORD`) 등 시크릿은 **환경변수/프로필로만** 주입하고 소스에 남기지 않는다.
- 각 서비스는 자신의 DB만 설정한다. DB 종류·마이그레이션은 서비스별로 독립 (예: `auth`는 RDB + Redis).

### V. 테스트 품질 게이트 (Test Quality Gate)

- 도메인·애플리케이션 로직은 JUnit 테스트로 검증한다.
- **JaCoCo 라인 커버리지 80% 이상**을 빌드 게이트로 강제한다. 이 기준을 낮추는 변경은 이 헌법의 개정(governance) 절차를 거친다.
- 서비스 간 계약(Feign 클라이언트/응답 DTO)이 바뀌면 해당 계약 지점의 테스트를 함께 갱신한다.

### VI. 스키마·문서와 코드의 일치 (Docs & Schema Stay Truthful)

- README/CONTRIBUTING 등 문서에 적힌 기술 사실(DB 종류, 인증 방식 등)은 코드와 어긋나면 안 된다. 코드 변경 시 관련 문서를 같은 PR에서 갱신한다.
- REST API는 **springdoc(OpenAPI)** 어노테이션으로 문서화한다.
- API 경로는 문자열 리터럴을 흩어놓지 않고 서비스별 `*ApiUrl` 상수로 중앙 관리한다.

### VII. 표준 API 계약 & 에러 규약 (Uniform API & Error Contract)

- **에러 응답 규약**: 도메인/애플리케이션 오류는 `ApplicationException` + `ErrorCode`(코드·메시지·`HttpStatus` 매핑)로 표현하고, `ApplicationExceptionHandler`가 처리한다. 현재 as-built 규약은 오류 코드·메시지를 응답 **헤더**(`code`, `message` — `StaticValues.HEADER_ERROR_*`)에 담고 본문은 비운다.
  - **모든 오류 응답은 이 규약 하나로 통일**한다. 특히 입력 검증 실패(`MethodArgumentNotValidException` 등 프레임워크 예외)도 동일 핸들러/형식으로 흡수해, "도메인 오류는 헤더, 검증 오류는 기본 본문"처럼 형식이 갈리지 않게 한다.
  - 규약을 바꾸려면(예: 헤더 → JSON 본문) 전 서비스 일괄 변경 + 이 헌법 개정으로만 한다.
- **입력 검증 위치**: 형식 검증(필수·길이·패턴 등)은 요청 DTO에서 Jakarta Bean Validation(`@Valid`)으로, 상태 의존적 불변식(유일성 등)은 도메인 서비스에서 수행한다.

## 기술 스택 제약 (Technology Constraints)

- 언어/런타임: **Java 19**, Spring Boot
- 빌드: **Gradle** (서비스별 `build.gradle` 멀티모듈)
- 영속성: **JPA** + **QueryDSL**(동적/복잡 조회), 매핑은 **MapStruct**
- 게이트웨이: **Spring Cloud Gateway** (리액티브)
- 서비스 간/외부 통신: **OpenFeign**(내부), **WebClient**(외부 PG)
- 결제 PG 확장은 **Strategy 패턴 + Factory**로 추가한다 (PG사별 분기 하드코딩 금지; 현재 Inicis/Nicepay).
- CI/CD: **GitHub Actions** + **Docker**

## 보안 요구사항 (Security Requirements)

- **자격증명·PII 위생**: 비밀번호 등 자격증명은 어떤 응답에도 포함하지 않으며(평문·해시 모두), 어떤 로그(성공/실패 포함)에도 남기지 않는다. 비밀번호는 항상 단방향 해시(BCrypt)로만 저장한다.
- **공개 엔드포인트 거버넌스**: 인증 없이 접근 가능한 경로(게이트웨이 화이트리스트 등록)를 새로 추가하려면, 해당 PR에 **공개 사유를 명시**하고 보안 관점 검토를 거친다. 화이트리스트 추가는 리뷰의 명시적 확인 항목이다.
- **최소 노출 응답**: 응답은 필요한 정보만 담는다. 내부 식별자(PK 등)나 구현 세부는 불필요하면 노출하지 않는다.

## 개발 워크플로우 (Development Workflow)

브랜치 전략:

```
master  ← 배포 (push 시 Docker 이미지 빌드·배포)
develop ← 통합 (PR 시 각 모듈 빌드·테스트 검증)
features/<branch-name> ← develop 기준 분기
```

- `develop` 기준으로 `features/<name>` 분기 → 개발 → `develop`으로 PR → Actions 빌드 성공 확인 → `develop` → `master` PR·머지.
- 커밋 메시지 규칙: `feat` / `fix` / `refactor` / `chore` / `docs`.

SDD 워크플로우 (신규 기능):

1. `/speckit-specify` — "무엇을/왜" 명세 작성 (기술 구현 배제)
2. `/speckit-plan` — 위 기술 스택 제약에 맞춰 "어떻게" 설계
3. `/speckit-tasks` — 실행 가능한 작업으로 분해
4. `/speckit-implement` — 구현

명세 배치 (MSA 정렬):
- 서비스 전용 명세는 해당 모듈 아래 `<module>/specs/<NNN>-<feature>/`에 둔다 (소유권 co-location, 서비스별 순번). 예: `user/specs/001-user-service/`.
- 여러 서비스에 걸친 횡단(cross-cutting) 명세만 루트 `specs/`에 둔다.
- 기존 서비스의 as-built 명세는 각 모듈의 `specs/`에 baseline(`001-*`)으로 보관한다.
- `/speckit-specify` 실행 시 `SPECIFY_FEATURE_DIRECTORY`를 모듈 경로로 지정한다(도구 기본값은 루트 `specs/`).

## Governance

- 이 헌법은 다른 관행보다 우선한다. 모든 PR/리뷰는 위 원칙 준수를 확인한다.
- NON-NEGOTIABLE로 표시된 원칙(II, III, VII의 에러 규약)을 벗어나는 구현은 명시적 사유와 대안 없이는 승인하지 않는다.
- 원칙을 바꾸려면 이 문서를 개정하고, 사유·영향 범위·마이그레이션 계획을 PR 설명에 남긴다.
- 복잡도를 추가하는 결정은 "왜 더 단순한 방법으로 안 되는가"를 근거로 정당화한다 (YAGNI 우선).

**Version**: 1.1.0 | **Ratified**: 2026-07-22 | **Last Amended**: 2026-07-22

<!--
개정 이력:
- 1.1.0 (2026-07-22): 002-user-signup dogfooding에서 발견된 갭 반영.
  · 원칙 VII(표준 API 계약 & 에러 규약) 신설 — 헤더 기반 에러 규약 통일, 검증 위치 명문화
  · 보안 요구사항 섹션 신설 — 자격증명·PII 위생, 공개 엔드포인트 거버넌스
  · SDD 워크플로우의 명세 배치를 모듈별(<module>/specs/)로 정정 (기존 "specs/" 서술이 실제 레이아웃과 불일치했음)
- 1.0.0 (2026-07-22): 기존 코드베이스에서 역방향 정립한 최초 헌법.
-->

