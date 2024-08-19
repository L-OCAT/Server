plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.flywaydb.flyway") version "10.15.0"
    id("jacoco")
}

group = "com.locat"
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

jacoco {
    toolVersion = "0.8.11"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}

val queryDSLVersion by extra("5.1.0")
val jjwtVersion by extra("0.11.5")
val j2htmlVersion by extra("1.6.0")
val flywayDBVersion by extra("10.15.0")
val awsSNSVersion by extra("2.21.42")

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation(platform("software.amazon.awssdk:bom:2.24.0"))
    implementation("software.amazon.awssdk:s3")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    runtimeOnly("com.mysql:mysql-connector-j")
    // Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:$queryDSLVersion:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:$queryDSLVersion:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    // Mail
    implementation("com.j2html:j2html:$j2htmlVersion")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // AWS SNS
    implementation("software.amazon.awssdk:sns:$awsSNSVersion")
    // DataBase Schema Migration
    implementation("org.flywaydb:flyway-mysql:$flywayDBVersion")
    implementation("org.flywaydb:flyway-core:$flywayDBVersion")
    // Local Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    // Testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // Test Containers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:localstack")
}

spotless {
    java {
        googleJavaFormat()
            .formatJavadoc(true)
        endWithNewline()
        formatAnnotations()
        removeUnusedImports()
        trimTrailingWhitespace()
    }
}

tasks.jar {
    isEnabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
//    finalizedBy (tasks.jacocoTestReport, tasks.jacocoTestCoverageVerification)
    reports {
        html.required.set(false)
        junitXml.required.set(false)
    }
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
        csv.required = false
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/xml/jacocoTestReport.xml"))
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
    }
    afterEvaluate {
        classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it).matching {
                    include("com/locat/api/**")
                }
            })
        )
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            enabled = false // 추후에 true로 바꾸기
            isFailOnViolation = true
            includes = listOf("com.locat.*")
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal(0.8)
            }
        }
    }
}
