object Versions {
    const val kotlin = "1.6.21"
    const val gradle = "7.0.3"
    const val kotlinCoroutines = "1.6.0"
    const val ktor = "1.6.5"
    const val kotlinxSerialization = "1.3.0"
    const val koin = "3.1.2"
    const val sqlDelight = "1.5.0"

    const val slf4j = "1.7.30"
    const val constraintLayout = "2.1.1"

    const val junit = "4.13"
    const val testRunner = "1.3.0"
    const val material = "1.4.0"
    const val appCompact = "1.3.1"
    const val coil = "1.4.0"
    const val accompanistCoil = "0.15.0"
    const val leakCanaryVersion = "2.7"
    const val dataStorePreferencesVersion = "1.0.0"

    const val analytics = "17.4.1"
    const val crashlytics = "17.0.0"
    const val firestore = "23.0.3"
    const val dynamicLinks = "20.1.1"

    const val googleServices = "4.3.10"

    const val navigation = "2.3.5"

    const val firebaseBom = "28.4.0"
    const val grpc = "1.40.0"

    const val accompanistVersion = "0.17.0"

    const val extensions = "2.2.0"
    const val viewmodel = "2.4.0"
    const val activity = "1.4.0"
    const val fragment = "1.3.6"
    const val lifecycle = "2.4.0"

    const val googleMap = "17.0.1"
    const val googleMapUtils = "2.2.3"
    const val googleMapUtilsKTX = "3.1.0"

    const val hilt = "2.40.1"
    const val hiltService = "2.40.1"

    const val fastAdapter = "5.3.4"

    const val roomDatabaseVersion = "2.3.0"

    const val crashlyticsClassPath = "2.8.1"

    const val joba = "2.10.9"

    const val materialComposeThemeAdapter = "1.1.4"
    const val compose = "1.2.0-rc02"
}

object Application {
    const val versionMajor = 1
    const val versionMinor = 0
    const val versionPatch = 3

    const val versionName = "${versionMajor}_${versionMinor}_$versionPatch"
    const val applicationId = "com.bunbeauty.fooddeliveryadmin"
    const val versionCode = 103
}

object AndroidSdk {
    const val min = 26
    const val compile = 32
    const val target = compile
}

object Time {
    const val jobaTime = "joda-time:joda-time:${Versions.joba}"
}

object ClassPath {
    const val buildTool = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val navigationServices =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

    const val hiltServices =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltService}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsClassPath}"

}

object Plugin {
    const val application = "com.android.application"
    const val android = "android"
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinParcelize = "kotlin-parcelize"
    const val kotlinSerialization = "kotlinx-serialization"
    const val kapt = "kotlin-kapt"
    const val navigation = "androidx.navigation.safeargs.kotlin"
    const val multiplatform = "multiplatform"
    const val nativeCocoapods = "native.cocoapods"
    const val hiltPlugin = "dagger.hilt.android.plugin"
    const val googleService = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
}

object AndroidX {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompact}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:1.8.0"
}

object Google {
    const val material = "com.google.android.material:material:${Versions.material}"
    const val playServices = "com.google.android.gms:play-services-maps:${Versions.googleMap}"
    const val androidMaps = "com.google.maps.android:android-maps-utils:${Versions.googleMapUtils}"
    const val mapsUtils = "com.google.maps.android:maps-utils-ktx:${Versions.googleMapUtilsKTX}"
}

object Dagger {
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
}

object FastAdapter {
    const val fastAdapter = "com.mikepenz:fastadapter:${Versions.fastAdapter}"
    const val fastAdapterBinding =
        "com.mikepenz:fastadapter-extensions-binding:${Versions.fastAdapter}"
}

object Accompanist {
    const val navigationAnimation =
        "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanistVersion}"
}

object SquareUp {
    const val leakCanary =
        "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanaryVersion}"
}

object Test {
    const val junit = "junit:junit:${Versions.junit}"
}

object Koin {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"
    const val test = "io.insert-koin:koin-test:${Versions.koin}"
    const val android = "io.insert-koin:koin-android:${Versions.koin}"
    const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
}

object Navigation {
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationTesting = "androidx.navigation:navigation-testing:${Versions.navigation}"
}

object Ktor {
    const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val clientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"

    const val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val clientIos = "io.ktor:ktor-client-ios:${Versions.ktor}"
    const val slf4j = "org.slf4j:slf4j-simple:${Versions.slf4j}"
}

object Coil {
    const val coil =
        "io.coil-kt:coil:${Versions.coil}"
}

object DataStore {
    const val dataStorePreferences =
        "androidx.datastore:datastore-preferences:${Versions.dataStorePreferencesVersion}"
}

object Grpc {
    const val grpc = "io.grpc:grpc-okhttp:${Versions.grpc}"
}

object Serialization {
    const val json =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
}

object Firebase {
    const val analytics = "com.google.firebase:firebase-analytics:${Versions.analytics}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.crashlytics}"
    const val firestore = "com.google.firebase:firebase-firestore-ktx:${Versions.firestore}"
    const val dynamicLink =
        "com.google.firebase:firebase-dynamic-links-ktx:${Versions.dynamicLinks}"
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
}

object SqlDelight {
    const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
    const val coroutineExtensions =
        "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

    const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    const val nativeDriverMacos =
        "com.squareup.sqldelight:native-driver-macosx64:${Versions.sqlDelight}"
    const val sqlliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
}

object Coroutine {
    const val coroutineCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
}

object RoomDatabase {
    const val roomDatabaseRuntime = "androidx.room:room-runtime:${Versions.roomDatabaseVersion}"
    const val roomDatabaseKtx = "androidx.room:room-ktx:${Versions.roomDatabaseVersion}"
    const val roomDatabaseKapt = "androidx.room:room-compiler:${Versions.roomDatabaseVersion}"
}

object Lifecycle {
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.extensions}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activity}"
    const val viewmodelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.viewmodel}"
}

object Compose {
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val animation = "androidx.compose.animation:animation:${Versions.compose}"
    const val ui = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val materialThemeAdapter =
        "com.google.android.material:compose-theme-adapter:${Versions.materialComposeThemeAdapter}"
}

