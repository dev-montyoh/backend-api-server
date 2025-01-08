#!/bin/bash

##  입력 변수 설정
SERVICE_NAME=$1
PROFILE=$2
VERSION=$3

##  Docker 이미지 빌드
echo "Building Docker image..."
docker build -f Dockerfile  \
-t "$SERVICE_NAME":"$VERSION" \
-t  "$SERVICE_NAME":latest  \
--build-arg SERVICE_NAME="$SERVICE_NAME"  \
--build-arg PROFILE="$PROFILE"  \
--build-arg VERSION="$VERSION"  \
../../