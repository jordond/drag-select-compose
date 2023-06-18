pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Drag Select Compose"

if (System.getenv()["JITPACK"] == null) {
    include(":demo:androidApp")
}

include(
    ":dragselectcompose",
    ":core",
    ":extensions",
    ":grid",
)
