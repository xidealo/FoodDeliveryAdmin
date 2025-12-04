import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.bunbeauty.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }
}

dependencies {
    implementation(gradleKotlinDsl())
    compileOnly(libs.gradle)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation.assign(true)
        failOnWarning.assign(true)
    }
}

gradlePlugin {
    plugins {
        register("androidFeature") {
            id = "com.bunbeauty.android.feature"
            version = "1.0"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidApplication") {
            id = "com.bunbeauty.android.application"
            version = "1.0"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
