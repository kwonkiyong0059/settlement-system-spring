# 1단계: 빌드 이미지 생성
FROM openjdk:21-jdk-slim AS build

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# Gradle Wrapper와 Gradle 디렉토리를 복사합니다.
COPY gradlew ./
COPY gradle ./gradle

# 소스 파일 및 Gradle 빌드 설정 파일을 복사합니다.
COPY src ./src
COPY build.gradle ./
COPY settings.gradle ./

# 필수 패키지를 설치합니다.
RUN apt-get update && apt-get install -y bash

# Gradle을 사용하여 프로젝트를 빌드합니다.
RUN ./gradlew build --no-daemon

# 2단계: 실행 이미지 생성
# JRE가 포함된 OpenJDK 21 slim 이미지를 사용하여 애플리케이션을 실행합니다.
FROM openjdk:21-jdk-slim

# 빌드 단계에서 생성된 JAR 파일을 복사합니다.
COPY --from=build /app/build/libs/ProjCalc-*.jar /app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]
