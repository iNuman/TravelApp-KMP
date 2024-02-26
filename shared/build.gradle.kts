@file:Suppress("OPT_IN_USAGE")

plugins {
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

version = "1.0"
val ktorVersion = extra["ktor.version"]

kotlin {
    androidTarget()
    jvm("desktop")
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
    }

    wasmJs {
        browser()
    }

    macosX64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    macosArm64 {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/SEAbdulbasit/TravelApp-KMP"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.runtime)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                //ktor-client
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$ktorVersion")
//                implementation("com.google.code.gson:gson:2.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }

        androidMain {
            dependencies {
                implementation("com.google.android.material:material:1.11.0")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
//                implementation("io.ktor:ktor-client-android:$ktorVersion")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation("io.ktor:ktor-client-java:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
            }
        }

        jsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
            }
        }

    }


}

android {
    compileSdk = 34
    namespace = "com.example.travelapp_kmp"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

compose {
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.20")
}

repositories {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
}