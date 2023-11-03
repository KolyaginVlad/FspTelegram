import java.net.URI

plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = URI.create("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.kodein.di:kodein-di:7.19.0")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("dev.inmo:tgbotapi:9.2.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(18)
}

application {
    mainClass.set("MainKt")
}