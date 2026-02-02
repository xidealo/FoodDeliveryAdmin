package com.bunbeauty.conventions

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.bunbeauty.AndroidSdk
import com.bunbeauty.configureKotlinAndroid
import com.bunbeauty.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(receiver = target) {
            with(receiver = pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jlleitschuh.gradle.ktlint")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                applyDefaultHierarchyTemplate()
                androidTarget()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidSdk.TARGET
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }

        }
    }
}