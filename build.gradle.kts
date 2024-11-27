import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "dev.sanson.aoc"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.kotest:kotest-assertions-core:5.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}