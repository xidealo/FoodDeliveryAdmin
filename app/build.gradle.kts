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
    namespace = Namespace.app

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
            storeFile = file(
                project.property("RELEASE_STORE_FILE") ?: error("RELEASE_STORE_FILE not found")
            )
            storePassword = project.property("RELEASE_STORE_PASSWORD").toString()
            keyAlias = project.property("RELEASE_KEY_ALIAS").toString()
            keyPassword = project.property("RELEASE_KEY_PASSWORD").toString()

            enableV1Signing = true
            enableV2Signing = true
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
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        buildFeatures {
            viewBinding = true
            compose = true
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
        composeOptions {
            kotlinCompilerExtensionVersion = Versions.composeCompiler
        }
    }
}

dependencies {
    implementation(project(":data")) // TODO remove
    implementation(project(":domain")) // TODO remove
    implementation(project(":common"))
    implementation(project(":presentation"))

    // Navigation
    implementation(Navigation.fragment)
    implementation(Navigation.ui)
    implementation(Navigation.runtime)

    // Firebase
    implementation(platform(Firebase.bom))
    implementation(Firebase.messaging)
    implementation(Firebase.crashlytics)

    // Lifecycle
    implementation(Lifecycle.service)

    // Material
    implementation(Material.material)

    // ViewBinding
    implementation(ViewBinding.propertyDelegate)

    // Compose
    implementation(Compose.bom)
    implementation(Compose.foundation)
    implementation(Compose.ui)
    implementation(Compose.material3)
    implementation(Compose.material)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.uiViewbinding)
    implementation(Compose.lifecycle)

    // Dagger Hilt
    implementation(Dagger.hilt)
    kapt(Dagger.hiltCompiler)

    // Coil
    implementation(Coil.coil)
    implementation(Coil.coilCompose)

}
