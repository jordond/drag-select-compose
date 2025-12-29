import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.publish)
}

kotlin {
    applyDefaultHierarchyTemplate()
    explicitApi()

    @Suppress("UnstableApiUsage")
    androidLibrary {
        compileSdk = libs.versions.sdk.compile.get().toInt()
        namespace = "com.dragselectcompose"
        minSdk = libs.versions.sdk.min.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    js(IR) {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

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
        commonMain.dependencies {
            api(project(":core"))
            api(project(":extensions"))
            api(project(":grid"))

            implementation(compose.runtime)
            implementation(compose.foundation)
        }
    }
}