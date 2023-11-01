plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.publish)
}

kotlin {
    explicitApi()

    androidTarget {
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
            }
        }
    }
}

android {
    compileSdk = libs.versions.sdk.compile.get().toInt()
    namespace = "com.dragselectcompose"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}