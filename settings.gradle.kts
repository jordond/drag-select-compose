pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.develocity") version "4.0.2"
}

develocity {
    buildScan {
        publishing.onlyIf { context ->
            context.buildResult.failures.isNotEmpty() && !System.getenv("CI").isNullOrEmpty()
        }
    }
}

rootProject.name = "DragSelectCompose"

include(":demo:android")
include(
    ":demo:kmm:shared",
    ":demo:kmm:desktopApp",
    ":demo:kmm:androidApp",
)

include(
    ":dragselect",
    ":core",
    ":extensions",
    ":grid",
)
