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
    runtimeOnly("org.kodein.di:kodein-di:7.18.0")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation ("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}