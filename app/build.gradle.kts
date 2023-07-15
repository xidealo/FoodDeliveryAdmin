plugins {
    id(Plugin.application)
    kotlin(Plugin.android)
    id(Plugin.kotlinAndroid)
    id(Plugin.kapt)
    id(Plugin.hiltPlugin)
    id(Plugin.navigation)
    id(Plugin.googleService)
    id(Plugin.crashlytics)
    id(Plugin.kotlinParcelize)
    id(Plugin.ktLint) version Versions.ktLint
}

android {

    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = Application.applicationId
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = Application.versionCode
        versionName = Application.versionName
        signingConfig = signingConfigs.getByName("debug")
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
            compose = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        composeOptions {
            kotlinCompilerExtensionVersion = Versions.composeCompiler
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":presentation"))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)

    // navigation
    implementation(Navigation.fragment)
    implementation(Navigation.ui)
    implementation(Navigation.runtime)

    // Firebase
    implementation(platform(Firebase.bom))
    implementation(Firebase.messaging)
    implementation(Firebase.crashlytics)

    // Lifecycle
    implementation(Lifecycle.lifecycleViewModel)
    implementation(Lifecycle.activity)
    implementation(Lifecycle.fragment)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.service)

    // Material
    implementation(Material.material)

    implementation(ViewBinding.propertyDelegate)

    // Compose
    implementation(Compose.bom)
    implementation(Compose.foundation)
    implementation(Compose.ui)
    implementation(Compose.material3)
    implementation(Compose.material)
    implementation(Compose.uiTooling)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.uiViewbinding)
    implementation(Compose.activity)
    implementation(Compose.lifecycle)

    // Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)

    // Image loader
    implementation(Coil.coil)
    implementation(Coil.coilCompose)

    // AdapterDelegates
    implementation(AdapterDelegates.adapterDelegates)
    implementation(AdapterDelegates.adapterDelegatesLayoutContainer)
    implementation(AdapterDelegates.adapterDelegatesViewbinding)
}
