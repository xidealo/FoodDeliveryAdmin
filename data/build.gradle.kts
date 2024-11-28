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
        lint.targetSdk = AndroidSdk.target
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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

    // Coroutine
    implementation(libs.kotlinx.coroutines.services)

    // Database
    implementation(libs.bundles.room)
    kapt(libs.room.database.kapt)

    // Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    // DataStore
    implementation(libs.datastore.preferences)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)


    implementation(libs.bundles.ktor)

}
