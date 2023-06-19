import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencies)

    val kotlinVersion = libs.versions.kotlin.get()
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(rootDir.resolve("dokka"))
}