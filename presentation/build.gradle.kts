plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
    id(Plugin.kotlinParcelize)
    id(Plugin.hiltPlugin)
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

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation(AndroidX.appCompat)
    implementation(Lifecycle.lifecycleViewModel)

    //Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)

    //DataStore
    implementation(DataStore.dataStorePreferences)
}