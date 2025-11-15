plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.data

    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

    // Work manager
    implementation(libs.work.runtime.ktx)

    // Coroutine
    implementation(libs.kotlinx.coroutines.services)

    // Database
    implementation(libs.bundles.room)
    kapt(libs.room.database.kapt)

    // Koin
    implementation(libs.bundles.di)

    // DataStore
    implementation(libs.datastore.preferences)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.bundles.ktor)

    // Testing
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockk)
}
