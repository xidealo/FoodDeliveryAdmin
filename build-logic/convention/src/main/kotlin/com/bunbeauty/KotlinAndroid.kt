package com.bunbeauty

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure base Kotlin with Android options for pure Android library modules.
 */
internal fun Project.configureKotlinAndroid(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
        compileSdk = AndroidSdk.COMPILE

        defaultConfig {
            minSdk = AndroidSdk.MIN
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    configureKotlin()
}

internal fun KotlinMultiplatformExtension.configureAndroidTarget(
    project: Project,
    configure: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
) {
    val androidTarget =
        (this as ExtensionAware).extensions.getByName("android")
            as KotlinMultiplatformAndroidLibraryTarget

    androidTarget.apply {
        compileSdk = AndroidSdk.COMPILE
        minSdk = AndroidSdk.MIN
        withJava()

        if (project.hasHostTestSources()) {
            withHostTest {}
        }

        if (project.hasDeviceTestSources()) {
            withDeviceTest {}
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        configure()
    }
}

private fun Project.hasHostTestSources(): Boolean =
    projectDir.resolve("src/androidHostTest").exists() ||
        projectDir.resolve("src/androidUnitTest").exists()

private fun Project.hasDeviceTestSources(): Boolean =
    projectDir.resolve("src/androidDeviceTest").exists() ||
        projectDir.resolve("src/androidTest").exists()

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-Xstring-concat=inline")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

internal fun Project.configureKotlinMultiplatform() {
    configureKotlin()
}
