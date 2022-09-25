object Versions {
    const val kotlin = "1.6.0"
    const val gradle = "7.0.3"
    const val kotlinCoroutines = "1.6.0"
    const val ktor = "1.6.5"
    const val constraintLayout = "2.1.1"
    const val material = "1.4.0"
    const val appCompact = "1.3.1"
    const val coil = "1.4.0"
    const val dataStorePreferencesVersion = "1.0.0"
    const val dynamicLinks = "20.1.1"
    const val googleServices = "4.3.10"
    const val navigation = "2.3.5"
    const val extensions = "2.2.0"
    const val viewmodel = "2.4.0"
    const val activity = "1.4.0"
    const val fragment = "1.3.6"
    const val lifecycle = "2.4.0"
    const val googleMapUtils = "2.2.3"
    const val hilt = "2.40.1"
    const val hiltService = "2.40.1"
    const val fastAdapter = "5.3.4"
    const val roomDatabaseVersion = "2.4.2"
    const val joba = "2.10.9"
    const val crashlytics = "2.8.1"
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
    const val crashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
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
    const val hiltPlugin = "dagger.hilt.android.plugin"
    const val googleService = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
}

object AndroidX {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompact}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:1.7.0"
}

object Google {
    const val material = "com.google.android.material:material:${Versions.material}"
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

object Navigation {
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object Ktor {
    const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    const val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
}

object Coil {
    const val coil = "io.coil-kt:coil:${Versions.coil}"
}

object DataStore {
    const val dataStorePreferences =
        "androidx.datastore:datastore-preferences:${Versions.dataStorePreferencesVersion}"
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
}
