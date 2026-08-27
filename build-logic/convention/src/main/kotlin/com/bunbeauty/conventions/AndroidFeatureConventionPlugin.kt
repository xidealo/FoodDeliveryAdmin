package com.bunbeauty.conventions

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.bunbeauty.configureKotlinAndroid
import com.bunbeauty.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(receiver = target) {
            with(receiver = pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                add(
                    configurationName = "testImplementation",
                    dependencyNotation = kotlin("test-junit5")
                )
            }

        }
    }
}
