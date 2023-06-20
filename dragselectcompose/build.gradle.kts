@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.publish)
    kotlin("multiplatform")
}

kotlin {
    explicitApi = ExplicitApiMode.Strict

    android {
        publishLibraryVariants("release")
    }
    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { framework ->
        framework.binaries.framework {
            baseName = "full"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":extensions"))
                api(project(":grid"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.appcompat)
                api(libs.core.ktx)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdk = libs.versions.sdk.compile.get().toInt()
    namespace = "com.dragselectcompose"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}