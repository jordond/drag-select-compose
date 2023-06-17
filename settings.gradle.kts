pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Drag Select Compose"

if (System.getenv()["JITPACK"] == null) {
    include(":demo")
}

include(
    ":dragselectcompose",
    ":core",
    ":extensions",
    ":grid",
)
