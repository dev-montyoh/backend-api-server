# Quickstart: 회원가입 검증

이 기능이 end-to-end로 동작하는지 확인하는 실행 시나리오. (구현 완료 후 사용)

## 전제

- `user` 서비스와 `gateway`, `auth` 서비스 기동
- user DB 접근 가능
- 게이트웨이 화이트리스트에 회원가입 경로 등록됨

## 시나리오 1 — 가입 후 로그인 (US1)

```bash
# 1) 회원가입
curl -i -X POST http://localhost:8080/api/user/v1/users \
  -H "Content-Type: application/json" \
  -d '{"loginId":"tester01","password":"pass1234"}'
# 기대: 201 Created, 본문에 userNo, 비밀번호 미포함

# 2) 방금 계정으로 로그인
curl -i -X POST http://localhost:8080/api/user/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{"loginId":"tester01","password":"pass1234"}'
# 기대: 200 OK + 토큰
```

## 시나리오 2 — 중복 아이디 (US2)

```bash
curl -i -X POST http://localhost:8080/api/user/v1/users \
  -H "Content-Type: application/json" \
  -d '{"loginId":"tester01","password":"pass1234"}'
# 기대: 409 Conflict (DUPLICATE_USER_INFO), 새 계정 미생성
```

## 시나리오 3 — 검증 실패 (US3)

```bash
# 빈 아이디
curl -i -X POST http://localhost:8080/api/user/v1/users \
  -H "Content-Type: application/json" -d '{"loginId":"","password":"pass1234"}'
# 기대: 400

# 짧은 비밀번호
curl -i -X POST http://localhost:8080/api/user/v1/users \
  -H "Content-Type: application/json" -d '{"loginId":"tester02","password":"123"}'
# 기대: 400, 계정 미생성
```

## 통과 기준

- 시나리오 1: 201 → 200(로그인) 연속 성공, 응답에 비밀번호 없음
- 시나리오 2: 409, DB 계정 수 불변
- 시나리오 3: 두 요청 모두 400, 계정 미생성
- (자동화) 위를 커버하는 컨트롤러/통합 테스트로 JaCoCo 80% 유지
