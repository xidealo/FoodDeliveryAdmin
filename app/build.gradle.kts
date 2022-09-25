plugins {
    id(Plugin.application)
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.kapt)
    id(Plugin.hiltPlugin)
    id(Plugin.navigation)
    id(Plugin.googleService)
    id(Plugin.crashlytics)
}

android {
    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = Application.applicationId
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = Application.versionCode
        versionName = Application.versionName
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore")
            storePassword = "itisBB15092019"
            keyAlias = "papakarloKey"
            keyPassword = "Itispapakarlo08062004"
        }
    }

    buildTypes {
        applicationVariants.all {
            val variant = this
            variant.outputs
                .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                .forEach { output ->
                    val outputFileName =
                        "Admin_${variant.baseName}_${Application.versionName}.apk"
                    println("OutputFileName: $outputFileName")
                    output.outputFileName = outputFileName
                }
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            isMinifyEnabled = false
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        buildFeatures {
            viewBinding = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":presentation"))

    implementation(Google.material)
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.constraintLayout)

    //navigation
    implementation(Navigation.navigationFragment)
    implementation(Navigation.navigationUi)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:26.7.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")

    //lifecycle
    implementation(Lifecycle.lifecycleExtensions)
    implementation(Lifecycle.lifecycleViewModel)
    implementation(Lifecycle.activity)
    implementation(Lifecycle.fragment)
    implementation(Lifecycle.lifecycleRuntime)

    // Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)

    // Coroutine
    implementation(Coroutine.coroutineCore)

    // Image loader
    implementation(Coil.coil)

    // FastAdapter
    implementation(FastAdapter.fastAdapter)
    implementation(FastAdapter.fastAdapterBinding)
}