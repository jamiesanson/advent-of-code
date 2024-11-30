plugins {
    alias(libs.plugins.kotlin)
}

group = "dev.sanson.aoc"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotest.assertions)
    implementation(libs.kotlinx.coroutines)
}

kotlin {
    jvmToolchain(21)
}