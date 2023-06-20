pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.9"
}

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}

rootProject.name = "DragSelectCompose"

if (System.getenv("CI") == null) {
    include(":demo:android")
    include(
        ":demo:kmm:shared",
        ":demo:kmm:desktopApp",
        ":demo:kmm:androidApp",
    )
}

include(
    ":dragselect",
    ":core",
    ":extensions",
    ":grid",
)
