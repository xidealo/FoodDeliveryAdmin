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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf("-Xstring-concat=inline")
    }
}
