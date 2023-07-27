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

allprojects {
    // Workaround for https://github.com/Kotlin/dokka/issues/2977.
    // We disable the C Interop IDE metadata task when generating documentation using Dokka.
    tasks.withType<AbstractDokkaTask> {
        @Suppress("UNCHECKED_CAST")
        val taskClass = Class.forName("org.jetbrains.kotlin.gradle.targets.native.internal.CInteropMetadataDependencyTransformationTask") as Class<Task>
        parent?.subprojects?.forEach {
            dependsOn(it.tasks.withType(taskClass))
        }
    }
}
