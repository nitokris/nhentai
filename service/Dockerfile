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

CMD ["sh", "-c", "java -Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTPS_PROXY_HOST} -Dhttps.proxyPort=${HTTPS_PROXY_PORT} -jar /app/app.jar"]
