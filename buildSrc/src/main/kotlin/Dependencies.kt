object Versions {
    const val kotlin = "1.7.21"
    const val gradle = "7.3.1"
    const val kotlinCoroutines = "1.6.4"
    const val androidxCore = "1.8.0"
    const val ktor = "2.1.2"
    const val constraintLayout = "2.1.4"
    const val material = "1.7.0"
    const val appCompact = "1.5.1"
    const val coil = "2.2.2"
    const val dataStorePreferencesVersion = "1.0.0"
    const val googleServices = "4.3.14"
    const val navigation = "2.5.3"
    const val extensions = "2.2.0"
    const val viewmodel = "2.4.0"
    const val activity = "1.4.0"
    const val fragment = "1.5.4"
    const val lifecycle = "2.4.0"
    const val googleMapUtils = "2.2.3"
    const val hilt = "2.44"
    const val fastAdapter = "5.3.4"
    const val adapterDelegates = "4.3.2"
    const val roomDatabaseVersion = "2.4.3"
    const val joda = "2.12.1"
    const val crashlytics = "2.9.2"
    const val firebase = "31.0.2"
}

object Application {
    const val versionMajor = 1
    const val versionMinor = 1
    const val versionPatch = 0

    const val versionName = "${versionMajor}.${versionMinor}.$versionPatch"
    const val applicationId = "com.bunbeauty.fooddeliveryadmin"
    const val versionCode = 110
}

object AndroidSdk {
    const val min = 26
    const val compile = 32
    const val target = compile
}

object Time {
    const val jodaTime = "joda-time:joda-time:${Versions.joda}"
}

object ClassPath {
    const val buildTool = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val navigationServices =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val hiltServices =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
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
    const val coreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
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

object AdapterDelegates {
    const val adapterDelegates =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.adapterDelegates}"
    const val adapterDelegatesLayoutContainer =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:${Versions.adapterDelegates}"
    const val adapterDelegatesViewbinding =
        "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.adapterDelegates}"
}

object Navigation {
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
}

object Ktor {
    const val clientWebsockets = "io.ktor:ktor-client-websockets:${Versions.ktor}"
    const val negotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val serializerJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val clientAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val clientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
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

object Firebase {
    const val bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val messaging = "com.google.firebase:firebase-messaging-ktx"
}
