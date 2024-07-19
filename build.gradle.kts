plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.flywaydb.flyway") version "10.15.0"
    id("jacoco")
}

group = "com.locat"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
        mavenBom("software.amazon.awssdk:bom:2.21.0")
    }
}

val jjwtVersion by extra("0.11.5")
val flywayDBVersion by extra("10.15.0")

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
//    implementation(platform("software.amazon.awssdk:bom"))
//    implementation("software.amazon.awssdk:dynamodb-enhanced")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // DataBase Schema Migration
    implementation("org.flywaydb:flyway-mysql:$flywayDBVersion")
    implementation("org.flywaydb:flyway-core:$flywayDBVersion")

    // Local Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    // Testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
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

tasks.withType<Test> {
    useJUnitPlatform()
    reports.html.required.set(false)
    reports.junitXml.required.set(false)
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.withType<Test> {
    finalizedBy (tasks.jacocoTestReport)
    useJUnitPlatform()
//    jacoco {
//        excludes.set(listOf("com/locat/ExcludedClass*", "com/locat/anotherpackage/*"))
//    }
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        csv.required = false
        html.required = true

//        xml.destination = file("${buildDir}/reports/jacoco/xml/jacocoTestReport.xml")
//        html.destination = file("${buildDir}/reports/jacoco/html")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = false // 추후에 true로 바꾸기
            element = "CLASS" // 커버리지 측정 대상

            limit {
                counter = "LINE"   // 커버리지 측정의 기준 단위
                value = "COVEREDRATIO"  // 커버리지 기준 측정 방식
                minimum = BigDecimal(0.8)
            }
        }
    }
}


