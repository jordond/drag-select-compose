# drag-select-compose Demo

## Before running!

- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 8 on your machine
- install
  the [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
- add `local.properties` file to the project root and set a path to Android SDK there
- run `./gradlew podInstall` in the project root

## Running

Open the project in Android Studio and let it sync the project. In the configuration dropdown should
be the following:

- demo.androidApp
- demo.ios
- demo.desktop
- demo.browser

You can select a configuration and run it, or follow the steps below.

### Android

To run the application on android device/emulator:

- open project in Android Studio and run imported android run configuration

To build the application bundle:

- run `./gradlew :demo:kmm:androidApp:assembleDebug`
- find `.apk` file in `demo/kmm/androidApp/build/outputs/apk/debug/composeApp-debug.apk`

### Desktop

Run the desktop application: `./gradlew :demo:kmm:desktopApp:run`

### iOS

To run the application on iPhone device/simulator:

- Open `demo/kmm/iosApp/iosApp.xcworkspace` in Xcode and run standard configuration
- Or
  use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
  for Android Studio

### Browser

You can run the browser application by executing the following command:

```bash
./gradlew :demo:kmm:shared:jsBrowserDevelopmentRun

```