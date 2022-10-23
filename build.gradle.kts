import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application

    id("com.expediagroup.graphql") version "6.2.5"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val koinVersion= "3.2.2"
val koinKtor= "3.2.2"

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

    // Koin Core features
    implementation ("io.insert-koin:koin-core:$koinVersion")
    // Koin Test features
    testImplementation ("io.insert-koin:koin-test:$koinVersion")
    // Koin for Ktor
    implementation ("io.insert-koin:koin-ktor:$koinKtor")
    // SLF4J Logger
    implementation ("io.insert-koin:koin-logger-slf4j:$koinKtor")


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
