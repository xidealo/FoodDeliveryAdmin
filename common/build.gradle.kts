plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ktLint)
}

android {
    namespace = Namespace.common

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
}
