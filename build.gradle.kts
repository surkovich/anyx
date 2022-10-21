import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application

    id("com.expediagroup.graphql") version "6.2.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.expediagroup", "graphql-kotlin-server", "6.2.5")
    implementation("com.graphql-java", "graphql-java-extended-scalars", "19.0")
    implementation("io.ktor", "ktor-server-core", "2.0.3")
    implementation("io.ktor", "ktor-server-netty", "2.0.3")
    implementation("ch.qos.logback", "logback-classic", "1.2.1")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", "1.6.4")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

graphql {
    schema {
        packages = listOf("com.marmotta.anyx.graphql")
    }
}
