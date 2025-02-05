plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.10"
    id("org.jlleitschuh.gradle.ktlint")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {

    val ktorVersion = "1.6.0"
    val junitVersion = "5.7.2"

    api(project(":libs:hops-common-fhir"))
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
    implementation("io.ktor:ktor-auth:1.6.0")
    implementation("no.nav.security:token-validation-ktor:1.3.7")
    implementation("org.apache.kafka:kafka-clients:2.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly("io.ktor:ktor-server-netty:$ktorVersion")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:6.6")
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion") { exclude("org.jetbrains.kotlin", "kotlin-test-junit") }
    testImplementation("no.nav.security:mock-oauth2-server:0.3.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
