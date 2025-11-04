# Base Image 설정
FROM openjdk:17-jdk-slim

# 입력 변수 설정
ARG SERVICE_NAME
ARG PROFILE

# 작업 디렉토리 생성
WORKDIR /app

# Springboot Jar 파일 복사
COPY ../../$SERVICE_NAME/build/libs/$SERVICE_NAME.jar app.jar
ENV SPRING_PROFILE_ACTIVE=$PROFILE
ENV FILE_ENCODING=UTF-8

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]