plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencies)
    alias(libs.plugins.binaryCompatibility)
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "androidApp",
            "desktopApp",
            "shared",
        ),
    )
}

dokka {
    dokkaPublications.html {
        outputDirectory.set(layout.projectDirectory.dir("dokka"))
    }
}

dependencies {
    dokka(project(":core"))
    dokka(project(":dragselect"))
    dokka(project(":extensions"))
    dokka(project(":grid"))
}