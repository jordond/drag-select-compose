plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":demo:kmm:shared"))
}

kotlin {
    jvmToolchain(11)
}

android {
    compileSdk = libs.versions.sdk.compile.get().toInt()
    namespace = "dev.jordond.dragselectcompose.demo"

    defaultConfig {
        applicationId = "dev.jordond.dragselectcompose.demo"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
