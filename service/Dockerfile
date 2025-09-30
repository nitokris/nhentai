# ---- 构建阶段 ----
FROM gradle:8.14.3-jdk21-corretto AS builder

WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

RUN gradle clean build -x test --no-daemon

# ---- 运行阶段 ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/app.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
