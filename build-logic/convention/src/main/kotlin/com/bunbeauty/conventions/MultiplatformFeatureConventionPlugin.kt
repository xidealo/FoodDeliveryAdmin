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
            configureCocoapodsDeploymentTargetFix()
        }
    }
}

/**
 * Kotlin CocoaPods synthetic Podfile only raises IPHONEOS_DEPLOYMENT_TARGET to 12.0 (KT-57741),
 * but current Xcode requires 15.0+. Bump synthetic Pods targets after podInstall.
 */
private fun Project.configureCocoapodsDeploymentTargetFix() {
    pluginManager.withPlugin("org.jetbrains.kotlin.native.cocoapods") {
        val minDeploymentTarget = "15.5"
        val bumpTask =
            tasks.register("bumpCocoapodsIosDeploymentTarget") {
                description =
                    "Raises synthetic CocoaPods IPHONEOS_DEPLOYMENT_TARGET to $minDeploymentTarget"
                doLast {
                    val pbxproj =
                        layout.buildDirectory
                            .file("cocoapods/synthetic/ios/Pods/Pods.xcodeproj/project.pbxproj")
                            .get()
                            .asFile
                    if (pbxproj.exists().not()) {
                        return@doLast
                    }

                    val original = pbxproj.readText()
                    val patched =
                        original.replace(
                            Regex("""IPHONEOS_DEPLOYMENT_TARGET = [0-9.]+;"""),
                            "IPHONEOS_DEPLOYMENT_TARGET = $minDeploymentTarget;",
                        )
                    if (patched != original) {
                        pbxproj.writeText(patched)
                    }
                }
            }

        tasks.matching { task -> task.name.startsWith("podInstall") }.configureEach {
            finalizedBy(bumpTask)
        }
        tasks.matching { task -> task.name.startsWith("podBuild") }.configureEach {
            dependsOn(bumpTask)
        }
        bumpTask.configure {
            mustRunAfter(tasks.matching { task -> task.name.startsWith("podInstall") })
        }
    }
}
