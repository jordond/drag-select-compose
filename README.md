<p>
    <a href="https://jitpack.io/#dev.jordond/drag-select-compose"><img src="https://jitpack.io/v/dev.jordond/drag-select-compose.svg"></a>
    <a href="https://github.com/jordond/drag-select-compose/actions/workflows/ci.yml"><img src="https://github.com/jordond/drag-select-compose/actions/workflows/ci.yml/badge.svg"></a>
    <img src="https://img.shields.io/github/license/jordond/drag-select-compose" />   
</p>

# Drag Select Compose

This is a library that allows you to easily implement a "Google Photos"-style multi-selection in your Compose apps.

You can view the KDocs at [docs.drag-select-compose.jordond.dev](https://docs.drag-select-compose.jordond.dev)

## Table of Contents

- [Inspiration](#inspiration)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Demo App](#demo-app)
- [License](#license)

## Inspiration



## Getting Started

First you need to add jitpack to either your root level `build.gradle.kts` or
your `settings.gradle.kts` file:

In `build.gradle.kts`:

```kotlin
allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the dependency to your app level `build.gradle.kts` file:

```kotlin
dependencies {
    // Includes the core functionality along with the extensions
    implementation("dev.jordond:drag-select-compose:drag-select-compose:1.0.0")
  
    // Or use the modules you want
    
    // Core functionality
    implementation("dev.jordond:drag-select-compose:core:1.0.0")

    // Optional extensions for adding semantics and toggle Modifiers to Grid items
    implementation("dev.jordond:drag-select-compose:extensions:1.0.0")
}
```

## Usage

## Demo App

A demo app is included in the `demo` module, run it by following these steps:

```shell
git clone git@github.com:jordond/drag-select-compose.git drag-select-compose
cd drag-select-compose
./gradlew assembleRelease
```

Then install the `demo/build/outputs/apk/release/demo-release.apk` file on your device.

## License

See [LICENSE](LICENSE)
