import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

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

configure(allprojects.filter { it.name != "androidApp" }) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"

            freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
        }
    }

    tasks.withType<DokkaMultiModuleTask>().configureEach {
        outputDirectory.set(rootDir.resolve("dokka"))
    }
}