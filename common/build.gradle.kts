plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kotlinParcelize)
    id(Plugin.kotlinSerialization)
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}