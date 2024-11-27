plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "dev.sanson.aoc"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.kotest:kotest-assertions-core:6.0.0.M1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

kotlin {
    jvmToolchain(21)
}