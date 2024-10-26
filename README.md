# 👩‍🍳 Food Delivery Admin App

The app allows restaurant managers to track orders and statistics, edit menus, send push notifications

## Release

Get it on [Google Play](https://play.google.com/store/apps/details?id=com.bunbeauty.fooddeliveryadmin)

## Stack

- [Kotlin](https://kotlinlang.org/): Programming language
- [Dagger 2](https://dagger.dev/): Dependency injection
    - [Hilt](https://dagger.dev/hilt/): Provides a standard way to incorporate Dagger dependency injection into an Android application
- Kotlinx
    - [Coroutines](https://github.com/Kotlin/kotlinx.coroutines): Multithreading
    - [Serialization](https://github.com/Kotlin/kotlinx.serialization): JSON serialization/deserailization
- [Ktor](https://ktor.io/): HTTP/WebSocket client
- [Room](https://developer.android.com/training/data-storage/room): Persistence library provides an abstraction layer over SQLite to allow fluent database access
- [Firebase](https://firebase.google.com/): Crashlytics & Receiving push notifications
- [Coil](https://coil-kt.github.io/coil/): Image loading
- [Jetpack compose](https://developer.android.com/jetpack/compose): UI
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation): Navigation
- [Joda-Time](https://www.joda.org/joda-time/): Handling of date and time

![Admin logo large](https://github.com/xidealo/FoodDeliveryAdmin/assets/36783631/5d4792ac-5753-4c83-b6e8-d5533ce787b3)

# Checks
To run Android Lint, use:
```sh
./gradlew :app:lint
```
To run ktlint lint, use:
```sh
./gradlew ktlintCheck
```
To fix codestyle ktlint lint, use:
```sh
./gradlew ktlintFormat
```
To run unit test:
```sh
./gradlew test
```
