# ---- 构建阶段 ----
FROM gradle:8.14.3-jdk21-corretto AS builder

WORKDIR /app

# 先拷贝依赖文件，提高缓存利用
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle build -x test --no-daemon --build-cache || true

# 拷贝源码并构建 jar
COPY src ./src
RUN gradle clean build -x test --no-daemon --build-cache

# ---- 运行阶段 ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
