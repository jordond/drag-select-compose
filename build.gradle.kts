import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}

configure(allprojects.filter { it.name != "demo" }) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"

            freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
        }
    }
}