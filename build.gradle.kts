@file:Suppress("UnstableApiUsage")

import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencies)
    alias(libs.plugins.binaryCompatibility)

    val kotlinVersion = libs.versions.kotlin.get()
    kotlin("multiplatform") version kotlinVersion apply false
    kotlin("jvm") version kotlinVersion apply false
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "android",
            "androidApp",
            "desktopApp",
            "shared",
        ),
    )
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(rootDir.resolve("dokka"))
}

// Temporary workaround for https://github.com/Kotlin/dokka/issues/2977#issuecomment-1567328937
subprojects {
    tasks {
        val taskClass = "org.jetbrains.kotlin.gradle.targets.native.internal." +
            "CInteropMetadataDependencyTransformationTask"

        @Suppress("UNCHECKED_CAST")
        withType(Class.forName(taskClass) as Class<Task>) {
            onlyIf { gradle.taskGraph.allTasks.none { it is AbstractDokkaTask } }
        }
    }
}