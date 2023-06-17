import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencies)
}

configure(allprojects.filter { it.name != "demo" }) {
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