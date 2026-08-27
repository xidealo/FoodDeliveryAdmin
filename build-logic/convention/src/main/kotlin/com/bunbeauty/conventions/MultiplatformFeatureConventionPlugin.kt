package com.bunbeauty.conventions

import com.bunbeauty.configureAndroidTarget
import com.bunbeauty.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(receiver = target) {
            with(receiver = pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                applyDefaultHierarchyTemplate()
                configureAndroidTarget(target)

                iosX64()
                iosArm64()
                iosSimulatorArm64()
            }

            configureKotlinMultiplatform()
        }
    }
}
