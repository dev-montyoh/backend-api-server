#!/bin/bash

##  입력 변수 설정
SERVICE_NAME=$1
PROFILE=$2
VERSION=$3

##  Docker 이미지 빌드
echo "Building Docker image..."
docker build -f Dockerfile  \
-t monty082/backend-api-server:"$VERSION" \
-t monty082/backend-api-server:latest  \
--build-arg SERVICE_NAME="$SERVICE_NAME"  \
--build-arg PROFILE="$PROFILE"  \
--build-arg VERSION="$VERSION"  \
../../