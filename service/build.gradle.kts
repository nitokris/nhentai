import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.10"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.nitokrisalpha"
version = "0.0.1-SNAPSHOT"
description = "ncomic-springboot"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
//    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(platform("org.http4k:http4k-bom:6.18.1.0"))
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")
    // https://mvnrepository.com/artifact/com.fleeksoft.ksoup/ksoup
    implementation("com.fleeksoft.ksoup:ksoup:0.2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.20.0")
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.spring)
    implementation(libs.exposed.time)
    implementation(libs.exposed.migration.jdbc)
// https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation(libs.sqlite.jdbc)

    // https://mvnrepository.com/artifact/org.drewcarlson/qbittorrent-client
    implementation("org.drewcarlson:qbittorrent-client:1.1.0-alpha02")
    implementation("org.drewcarlson:qbittorrent-models:1.1.0-alpha02")
    implementation("io.ktor:ktor-client-okhttp:3.0.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.withType<BootJar> {
    archiveFileName.set("app.jar")
    mainClass.set("com.nitokrisalpha.NComicSpringbootApplicationKt")
}
