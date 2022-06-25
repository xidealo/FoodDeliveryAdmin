plugins {
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.androidLibrary)
    id(Plugin.kapt)
}

android {
    compileSdk = AndroidSdk.compile
    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    //compose
    implementation(Lifecycle.activityCompose)
    implementation(Compose.material)
    implementation(Compose.animation)
    implementation(Compose.ui)
    implementation(Lifecycle.viewmodelCompose)
    implementation(Compose.materialThemeAdapter)
}