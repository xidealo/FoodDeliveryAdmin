object Versions {
    const val kotlin = "1.9.0"
    const val gradle = "8.1.1"
    const val ktor = "2.3.2"
    const val coil = "2.4.0"
    const val datastorePreferences = "1.0.0"
    const val googleServices = "4.3.15"
    const val navigation = "2.7.0"
    const val activity = "1.7.2"
    const val fragment = "1.5.7"
    const val runtime = "1.5.1"
    const val lifecycle = "2.6.1"
    const val material = "1.9.0"
    const val viewBindingDelegate = "1.5.3"
    const val hilt = "2.47"
    const val room = "2.6.0-alpha02"
    const val joda = "2.12.5"
    const val crashlytics = "2.9.2"
    const val firebase = "32.2.0"
    const val composeCompiler = "1.5.0"
    const val composeBom = "2024.05.00"
    const val lifecycleRuntimeCompose = "2.6.1"
    const val ktLint = "11.5.1"
    const val mockk = "1.12.5"
    const val coroutines = "1.8.0"
    const val kotlinxDatetime = "0.4.0"
    const val materialDialogsDatetime = "0.9.0"
    const val kotlinxCollectionsImmutable = "0.3.7"
}

object Application {
    const val versionMajor = 1
    const val versionMinor = 5
    const val versionPatch = 0

    const val versionName = "${versionMajor}.${versionMinor}.$versionPatch"
    const val applicationId = "com.bunbeauty.fooddeliveryadmin"
    const val versionCode = 150
}

object Namespace {
    const val app = "com.bunbeauty.fooddeliveryadmin"
    const val presentation = "com.bunbeauty.presentation"
    const val domain = "com.bunbeauty.domain"
    const val data = "com.bunbeauty.data"
    const val common = "com.bunbeauty.common"
}

object AndroidSdk {
    const val min = 26
    const val compile = 34
    const val target = compile
}

object Time {
    const val jodaTime = "joda-time:joda-time:${Versions.joda}"
}

object ClassPath {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
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
    const val kotlinParcelize = "kotlin-parcelize"
    const val kotlinSerialization = "kotlinx-serialization"
    const val kapt = "kotlin-kapt"
    const val navigation = "androidx.navigation.safeargs.kotlin"
    const val hiltPlugin = "dagger.hilt.android.plugin"
    const val googleServices = "com.google.gms.google-services"
    const val crashlytics = "com.google.firebase.crashlytics"
    const val ktLint = "org.jlleitschuh.gradle.ktlint"
}

object Dagger {
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
}

object KotlinxDatetime {
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}"
}

object Navigation {
    const val fragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val runtime =
        "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
}

object Ktor {
    const val clientWebsockets = "io.ktor:ktor-client-websockets:${Versions.ktor}"
    const val clientContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val clientAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val clientOkhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
}

object Coil {
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilCompose = "io.coil-kt:coil-compose:${Versions.coil}"
}

object DataStore {
    const val dataStorePreferences =
        "androidx.datastore:datastore-preferences:${Versions.datastorePreferences}"
}

object RoomDatabase {
    const val roomDatabaseRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomDatabaseKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomDatabaseKapt = "androidx.room:room-compiler:${Versions.room}"
}

object Lifecycle {
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"
}

object Material {
    const val material = "com.google.android.material:material:${Versions.material}"
}

object ViewBinding {
    const val propertyDelegate =
        "com.github.kirich1409:viewbindingpropertydelegate:${Versions.viewBindingDelegate}"
}

object Compose {
    const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val foundation = "androidx.compose.foundation:foundation"
    const val ui = "androidx.compose.ui:ui"
    const val material3 = "androidx.compose.material3:material3"
    const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class"
    const val material = "androidx.compose.material:material"
    const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val uiTooling = "androidx.compose.ui:ui-tooling"
    const val uiViewbinding = "androidx.compose.ui:ui-viewbinding"
    const val lifecycle =
        "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeCompose}"
}

object MaterialDialogs {
    const val datetime =
        "io.github.vanpra.compose-material-dialogs:datetime:${Versions.materialDialogsDatetime}"
}

object Firebase {
    const val bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val messaging = "com.google.firebase:firebase-messaging-ktx"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val storage = "com.google.firebase:firebase-storage"
}

object Mockk {
    const val main = "io.mockk:mockk:${Versions.mockk}"
    const val common = "io.mockk:mockk-common:${Versions.mockk}"
}

object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object Test {
    const val test = "test"
}

object KotlinxCollectionsImmutable {
    const val collectionsImmutable =
        "org.jetbrains.kotlinx:kotlinx-collections-immutable:${Versions.kotlinxCollectionsImmutable}"
}