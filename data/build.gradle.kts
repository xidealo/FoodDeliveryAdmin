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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
        }
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

    // Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

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
