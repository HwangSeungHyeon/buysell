import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.noarg") version "1.8.22"
    kotlin("kapt") version "1.8.22"
}

group = "com.teamsparta"
version = "0.0.1-SNAPSHOT"

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val queryDslVersion = "5.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0") //swager
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") //jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc") //jdbc
    implementation("org.springframework.boot:spring-boot-starter-security") //security
    implementation("org.springframework.boot:spring-boot-starter-mail") //mail
    implementation("org.springframework.boot:spring-boot-starter-data-redis") //redis
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE") //s3
    implementation("org.springframework.boot:spring-boot-starter-aop") //aop
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    //oauth2 소셜로그인
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    //querydsl
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta") // 추가!
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta") // 추가!
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation ("org.slf4j:slf4j-api:1.7.30")
    implementation ("ch.qos.logback:logback-classic:1.2.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
