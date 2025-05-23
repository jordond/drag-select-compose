plugins {
    alias(libs.plugins.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    js(IR) {
        browser()
        binaries.executable()
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }

        commonMain.dependencies {
            implementation(project(":dragselect"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.coil)
            implementation(libs.coil.ktor)
            implementation(libs.ktor.core)
        }

        androidMain.dependencies {
            api(libs.activity.compose)
            api(libs.appcompat)
            api(libs.core.ktx)
            implementation(libs.ktor.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.darwin)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(libs.ktor.java)
        }
    }
}

android {
    compileSdk = libs.versions.sdk.compile.get().toInt()
    namespace = "com.dragselectcompose.demo"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(jdkVersion = 17)
    }
}